function handleSubmit(xhr, status, args, dialog) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        dialog.hide();
    }
}

function submit(xhr, status, args, dialog) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    }
}

function submitConfirmar(xhr, status, args, dialog, confirmar) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    }else{
        dialog.hide();
        confirmar.show();
    }
}


function mostrarDialog(xhr,status,args,dialog ,dialogUnitario) {
    if(!args.validationFailed ){
        if(args.mostrar){
            dialog.show();
        }
        if(args.mostrarDialogCodigoUnidad){
                dialogUnitario.show();
        }
    }
}


function mostrarCodigosDialog(args, dialogUnidad , dialogCaja ) {
    if(!args.vacio) {
        if(args.leerCaja){
            dialogCaja.show();
        }else{
            dialogUnidad.show();
        }
    } 
}


function codigoBarra(args, dialogProducto, dialogCaja ) {
    var jqDialog = jQuery('#'+dialogProducto.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
        dialogProducto.hide();
        dialogProducto.show();
    }else{
        if(args.vacio){
            dialogProducto.hide();
        }else{
            if(args.leerCaja){
                dialogProducto.hide();
                dialogCaja.show();
            }else{
                dialogProducto.hide();
                dialogProducto.show();
            }
            
        }
    }
}

function codigoBarraCaja(args, dialogCaja , dialogProducto) {
    var jqDialog = jQuery('#'+dialogCaja.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
        dialogCaja.hide();
        dialogCaja.show();
    }else{
        dialogCaja.hide();
    }
}


function editarUnidad(args, dialogProducto) {
    var jqDialog = jQuery('#'+dialogProducto.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    }
}


function muestreo(args, dialogCodigo , dialogArticulo , cajaMuestra) {
    var jqDialog = jQuery('#'+dialogCodigo.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
        dialogCodigo.hide();
        dialogCodigo.show();
    }else{
        if(args.caja){
            dialogCodigo.hide();
            cajaMuestra.show();
        }else{
            dialogCodigo.hide();
            dialogArticulo.show();
        }
    }
}


function handleSubmitMuestra(args, dialog) {
    var jqDialog = jQuery('#'+dialog.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        dialog.hide();
    }
}


function listarArticuloDespacho(args,dialogBusqueda, dialogConfirmarListar, dialogConfirmarCompleta , dialogConfirmarCaja) {
    var jqDialog = jQuery('#'+dialogBusqueda.id);
    if(args.validationFailed ) {
        dialogBusqueda.hide();
        dialogBusqueda.show();
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        if(args.caja){
            if(args.existe){
                if(args.cajaVacia){
                    //Caja Vacia
                    dialogBusqueda.hide();
                    dialogBusqueda.show();
                    jqDialog.effect('shake', { times:3 }, 100);
                }else{
                    //caja existe en el inventario y no esta vacia mostrar detalle y confirmar
                    dialogBusqueda.hide();
                    dialogConfirmarCaja.show();
                }
            }else{
                //No existe en inventario o ya esta listada
                dialogBusqueda.hide();
                dialogBusqueda.show();
                jqDialog.effect('shake', { times:3 }, 100);
            }
        }else{
            if(args.existe){
                dialogBusqueda.hide();
            }else{
                //No Existe en Inventario
                dialogBusqueda.hide();
                dialogBusqueda.show();
                jqDialog.effect('shake', { times:3 }, 100);
            }
        }
    }
}



function confirmarCajaCompleta(args,dialogBusqueda, dialogConfirmarListar, dialogConfirmarCompleta) {
    if(args.confirmar){
       dialogConfirmarCompleta.hide();
       dialogConfirmarListar.show();
    }else{
        dialogConfirmarCompleta.hide();
        dialogBusqueda.show();
    }
}



function listarCajaCompleta(args,dialogListarCaja, dialogMostrarNoListado, dialogBuscarCalzado) {
    if(args.hayNoListados){
       dialogListarCaja.hide();
       dialogMostrarNoListado.show();
    }else{
        dialogListarCaja.hide();
        dialogBuscarCalzado.show();
    }
}

function listarParteCaja(args,dialogListarCaja, dialogMostrarDetalle) {
    if(args.mostrar){
       dialogListarCaja.hide();
       dialogMostrarDetalle.show();
    }else{
        var jqDialog = jQuery('#'+dialogListarCaja.id);
        jqDialog.effect('shake', { times:3 }, 100);
    }
}


function mostrarConfirmarPrecios(args,dialogEditar, dialogConfirmar) {
    var jqDialog = jQuery('#'+dialogEditar.id);
    if(!args.validationFailed ){
        if(args.mostrar){
            dialogEditar.hide();
            dialogConfirmar.show();
        }else{
            jqDialog.effect('shake', { times:3 }, 100);
        }
    }else{
        jqDialog.effect('shake', { times:3 }, 100);
    }
}


function mostrarCodigoConfirmacion(args, dialogActual , dialogConfirmacion) {
    if(args.mostrarConfirmacion){
        dialogActual.hide();
        dialogConfirmacion.show();
    }else{
        dialogActual.hide();
    }
}


function buscarCambioDevolucion(xhr, status, args,dialogBuscar, dialogUnidad, dialogCaja){
    var jqDialog = jQuery('#'+dialogBuscar.id);
    if(args.validationFailed  || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
    } else {
        if(args.caja){
            dialogCaja.show();
         }else{
             dialogUnidad.show();
         }
    }
    
}

function mostrarDialogConfirmarCambio(xhr,status,args,dialogConfirm,dialog) {
    if(!args.validationFailed ){
        if(args.mostrar){
            dialogConfirm.show();
        }
    }else{
        var jqDialog = jQuery('#'+dialog.id);
        jqDialog.effect('shake', { times:3 }, 100);
    }
}

function listarCambio(xhr, status, args, dialogConfirm, dialog) {
    var jqDialog = jQuery('#'+dialogConfirm.id);
    if(args.validationFailed || !args.validar) {
        jqDialog.effect('shake', { times:3 }, 100);
        
    }else{
        dialogConfirm.hide();
        dialog.hide();
    }
}



function imprimirFactura() {
    var x = document.getElementById('factura');
    x.focus();
    x.printWithDialog();
}




function printfile(){ 
    window.frames['objAdobePrint'].focus();
    window.frames['objAdobePrint'].print();
} 