import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SecretariaCobroService } from '../../../services/secretaria-cobro.service';

@Component({
  selector: 'app-panel-cobros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel-cobros.component.html'
})

export class PanelCobrosComponent implements OnInit {
  @Input() estudiante: any;
  @Input() idSecretaria: number = 1;
  @Output() onVolver = new EventEmitter<void>();

  cargosPendientes: any[] = [];
  procesandoId: number = 0;
  mensajeExito: string = '';
  mensajeError: string = '';
  historialPagos: any[] = [];
  meses: string[] = ['', 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];


  constructor(
    private cobroService: SecretariaCobroService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarPendientes();
    this.cargarHistorial();
  }


  cargarPendientes() {
    this.cobroService.obtenerPendientes(this.estudiante.idEstudiante).subscribe(res => {
      this.cargosPendientes = res;
      this.cdr.detectChanges();
    });
  }


  cobrar(cargo: any) {
    this.procesandoId = cargo.idCargo;
    this.mensajeError = '';    
    const payload = {
      idEstudiante: this.estudiante.idEstudiante,
      idCargo: cargo.idCargo,
      idSecretaria: this.idSecretaria
    };
    this.cobroService.procesarPago(payload).subscribe({
      next: (res) => {
        this.procesandoId = 0;
        this.mensajeExito = `Cobro exitoso: Q${cargo.montoCobrado.toFixed(2)}`;
        this.cargarPendientes();
        this.cargarHistorial();        
        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.detectChanges();
        }, 5000);
      },
      error: (err) => {
        this.procesandoId = 0;
        this.mensajeError = err.error?.mensaje || 'Error al procesar el pago.';
        this.cdr.detectChanges();
      }
    });
  }


  obtenerNombreMes(num: number): string {
    return (num > 0 && num <= 12) ? this.meses[num] : 'N/A';
  }


  cargarHistorial() {
    this.cobroService.obtenerHistorial(this.estudiante.idEstudiante).subscribe(res => {
      this.historialPagos = res;
      this.cdr.detectChanges();
    });
  }


  imprimirFactura(pago: any) {
    this.cobroService.descargarReciboPdf(pago.idPago).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
        setTimeout(() => window.URL.revokeObjectURL(url), 10000);
      },
      error: (err) => {
        this.mensajeError = 'Error al generar el recibo PDF.';
        this.cdr.detectChanges();
      }
    });
  }


  imprimirSolvencia() {
    this.cobroService.descargarSolvenciaPdf(this.estudiante.idEstudiante).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
        setTimeout(() => window.URL.revokeObjectURL(url), 10000);
      },
      error: (err) => {
        this.mensajeError = 'Error al generar la solvencia. Verifique que el estudiante no tenga multas o pagos pendientes.';
        this.cdr.detectChanges();
      }
    });
  }
  
}