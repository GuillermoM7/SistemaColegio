import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SecretariaCobroService } from '../../../services/secretaria-cobro.service';

@Component({
  selector: 'app-panel-pagos-varios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './panel-pagos-varios.component.html'
})
export class PanelPagosVariosComponent implements OnInit {
  @Input() estudiante: any;
  @Input() idSecretaria: number = 1;
  @Output() onVolver = new EventEmitter<void>();

  // Estado para Multas
  multasPendientes: any[] = [];
  procesandoMultaId: number = 0;

  // Estado para Extracurriculares
  descExtra: string = '';
  montoExtra: number | null = null;
  procesandoExtra: boolean = false;

  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(
    private cobroService: SecretariaCobroService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.cargarMultas();
  }


  cargarMultas() {
    this.cobroService.obtenerMultasPendientes(this.estudiante.idEstudiante).subscribe(res => {
      this.multasPendientes = res;
      this.cdr.detectChanges();
    });
  }


  cobrarMulta(multa: any) {
    this.procesandoMultaId = multa.idCargo; 
    this.mensajeError = '';    
    const payload = {
      idEstudiante: this.estudiante.idEstudiante,
      idMulta: multa.idCargo,
      idSecretaria: this.idSecretaria,
      monto: multa.montoCobrado,
      concepto: multa.concepto
    };
    this.cobroService.procesarPagoMulta(payload).subscribe({
      next: (res) => {
        this.procesandoMultaId = 0;
        this.mostrarExito(`Multa cobrada exitosamente: Q${multa.montoCobrado.toFixed(2)}`);
        this.cargarMultas();
      },
      error: (err) => {
        this.procesandoMultaId = 0;
        this.mostrarError(err.error?.mensaje || 'Error al procesar la multa.');
      }
    });
  }


  cobrarExtra() {
    if (!this.descExtra.trim() || !this.montoExtra || this.montoExtra <= 0) {
      this.mostrarError('Debe ingresar una descripción válida y un monto mayor a 0.');
      return;
    }
    this.procesandoExtra = true;
    this.mensajeError = '';


    const payload = {
      idEstudiante: this.estudiante.idEstudiante,
      idSecretaria: this.idSecretaria,
      descripcion: this.descExtra,
      monto: this.montoExtra
    };


    this.cobroService.procesarPagoExtra(payload).subscribe({
      next: (res) => {
        this.procesandoExtra = false;
        this.mostrarExito(`Cobro extracurricular registrado: Q${this.montoExtra?.toFixed(2)}`);
        this.descExtra = '';
        this.montoExtra = null;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.procesandoExtra = false;
        this.mostrarError(err.error?.mensaje || 'Error al procesar el pago extracurricular.');
      }
    });
  }


  mostrarExito(msg: string) {
    this.mensajeExito = msg;
    this.mensajeError = '';
    this.cdr.detectChanges();
    setTimeout(() => {
      this.mensajeExito = '';
      this.cdr.detectChanges();
    }, 5000);
  }


  mostrarError(msg: string) {
    this.mensajeError = msg;
    this.mensajeExito = '';
    this.cdr.detectChanges();
  }
}