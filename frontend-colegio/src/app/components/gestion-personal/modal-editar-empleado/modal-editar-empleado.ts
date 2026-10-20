import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { ListadoEmpleadoDTO } from '../../../services/gestion-personal';

@Component({
    selector: 'app-modal-editar-empleado',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './modal-editar-empleado.html'
})
export class ModalEditarEmpleado implements OnInit {
    @Input() empleadoOriginal!: ListadoEmpleadoDTO;

    @Output() onCerrar = new EventEmitter<void>();
    @Output() onGuardar = new EventEmitter<ListadoEmpleadoDTO>();


    empleadoEditado: any = {};

    errorSueldo: boolean = false;

    ngOnInit() {
    this.empleadoEditado = { ...this.empleadoOriginal };
    }

    guardarCambios() {
        if (this.empleadoEditado.salario <= 0) {
        this.errorSueldo = true;
        return; 
        }
    
        this.errorSueldo = false;
        this.onGuardar.emit(this.empleadoEditado);
    }

    cerrarModal() {
        this.onCerrar.emit();
    }
}