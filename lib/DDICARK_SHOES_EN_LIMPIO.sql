/* ============================================================== */
/* DBMS name: MySQL 5.0 */
/* Created on: 11/07/2013 05:24:31 p.m. */
/* ============================================================== */

drop table if exists permisos;

drop table if exists devolucion_facturas;

drop table if exists devolucion;

drop table if exists detalle_envio;


drop table if exists pago_venta;

drop table if exists detalle_factura;

drop table if exists factura;

drop table if exists inventario;

drop table if exists caja;

drop table if exists preingreso;

drop table if exists detalle_pedido;

drop table if exists producto;

drop table if exists venta;

drop table if exists pago_compra;

drop table if exists ingreso;
 
drop table if exists envio;

drop table if exists pedido;

drop table if exists factura_ingreso;

drop table if exists retaceo;

drop table if exists proveedor;

drop table if exists transaccion;

drop table if exists cliente;

drop table if exists empleado;

drop table if exists configuraciones;

drop table if exists municipios;

/*==============================================================*/
/* Table: CONFIGURACIONES */
/*==============================================================*/
create table configuraciones
(
   NUMCONFIG int not null,
   TIPO                        varchar(30) not null,
   NOMBRE varchar(50) not null,
   VALOR_STRING varchar(50),
   VALOR_INT int default 0,
   VALOR_FLOAT float default 0,
   VALOR_DECIMAL decimal(10,2) default 0,                
   primary key (NUMCONFIG)
);


/*==============================================================*/
/* Table: ZONAS,DEPTO Y MUNICIPIOS */
/*==============================================================*/
create table municipios
(
   NUM         int not null,
   ZONA                        varchar(15) not null,
   DEPARTAMENTO varchar(25) not null,        
   MUNICIPIO                varchar(40) not null,
   primary key (NUM)
);

/*==============================================================*/
/* Table: CAJA */
/*==============================================================*/
create table caja
(
   NUMCAJA char(10) not null,
   TIPO_PRODUCTO int,
   ESTILO varchar(15),
   COLORES                text,
   DETALLE_CAJA text,
   UNIDADES_CAJA int not null,
   COSTO_UNITARIO decimal(10,2) ,
   COSTO_UNITARIO_REAL decimal(10,2) ,
   COSTO_CAJA decimal(10,2) not null,
   PRECIOVENTA_UNIDAD decimal(10,2) default 0,
   UBICACION_CAJA text,
   COMPLETA tinyint(1) not null,
   primary key (NUMCAJA)
);

/*==============================================================*/
/* Table: CLIENTE */
/*==============================================================*/
create table cliente
(
   NUMCUENTA int not null,
   NOMBRE_CLIENTE varchar(30),
   APELLIDO_CLIENTE varchar(25),
   DUI_CLIENTE varchar(10),
   NIT_CLIENTE varchar(17),
   CARNET_RESIDENTE varchar(15),
   GENERO_CLIENTE char(1),
   TEL_CLIENTE varchar(9),
   DIRECCION_CLIENTE text not null,
   MUNICIPIO_CLIENTE varchar(30) not null,
   DEPTO_CLIENTE varchar(25) not null,
   OCUPACION_CLIENTE varchar(20),
   COMERCIO_CONTACTO varchar(30),
   NRC_COMERCIO varchar(12),
   TEL_COMERCIO varchar(9),
   EMPLEADOASIGNADO char(5),
   NATURALEZA varchar(20) not null,
   primary key (NUMCUENTA)
);



/*==============================================================*/
/* Table: EMPLEADO */
/*==============================================================*/
create table empleado
(
   CODEMPLEADO char(5) not null,
   NOMBRE_EMPLEADO varchar(30) not null,
   APELLIDO_EMPLEADO varchar(25) not null,
   DUI_EMPLEADO varchar(10),
   NIT_EMPLEADO varchar(17),
   TEL_EMPLEADO varchar(9),
   FECHANAC_EMPLEADO date,
   PUESTO_EMPLEADO varchar(20) not null,
   USERNAME                varchar(20) not null ,
   PASSWORD                varchar(32) not null,
   ACTIVO tinyint(1) default 1 not null,
   primary key (CODEMPLEADO)
);




/*==============================================================*/
/* Table: PERMISOS*/
/*==============================================================*/
create table permisos
(
   IDPERMISO     	int not null,
   EMPLEADO             char(5) not null,
   MENU                 varchar(50) not null,
   SUBMENU		varchar(50) not null,
   DESCRIPCION		text,
   PERMISO  		tinyint(1)  default 0 not null,
   primary key (IDPERMISO)
);



/*==============================================================*/
/* Table: RETACEO */
/*==============================================================*/
create table retaceo
(
   NUMRETACEO varchar(10) not null,
   MONTO_FACTURAS decimal(10,2) not null,
   MONTO_SALIDA         decimal(10,2) not null,
   MONTO_DESCUENTOS        decimal(10,2) default 0,
   MONTO_TOTAL_FACTURAS decimal(10,2) default 0,
   MONTO_FLETE                decimal(10,2) default 0,
   MONTO_AGENTE_ADUANAL decimal(10,2) default 0,
   MONTO_ARANCEL        decimal(10,2) default 0,
   MONTO_TRANSPORTE decimal(10,2) default 0,
   MONTO_BODEGAJE        decimal(10,2) default 0,
   OTROS_GASTOS_CCF decimal(10,2) default 0,
   FACTOR_RETACEO decimal(10,4) default 0,
   MONTO_SEGURIDAD decimal(10,2) default 0,
   MONTO_SEGURO decimal(10,2) default 0,
   OTROS_GASTOS decimal(10,2) default 0,
   FACTOR_COSTO_VIAJE decimal(10,4) default 0,
   NOTA_RETACEO         text,
   APROBADO tinyint(1) default 0,
   IDTRANSAC int not null,
   primary key (NUMRETACEO)
);




/*==============================================================*/
/* Table: INGRESO */
/*==============================================================*/
create table ingreso
(
   NUMINGRESO int not null,
   IDTRANSAC int not null,
   DOCUMENTO_COMPRA         varchar(20) not null,
   FECHA_DOCUMENTO date not null,
   FECHA_REGISTRO         date not null,
   NOTA_INGRESO                        text,
   primary key (NUMINGRESO)
);

/*==============================================================*/
/* Table: FACTURA_INGRESO */
/*==============================================================*/

create table factura_ingreso(
   DOCUMENTO_COMPRA                 varchar(20) not null,
   FECHA_DOCUMENTO         date not null,
   TIPO_DOCUMENTO varchar(10),
   NUMRETACEO         varchar(10) not null,
   NATURALEZA_COMPRA varchar(10) not null,
   PROVEEDOR int not null,
   TIPO_COMPRA         varchar(10) default 'AL CONTADO',
   TOTAL_UNIDADES_COMPRA int not null,
   TOTAL_BULTOS_COMPRA int default 0,
   MONTO_COMPRA                 decimal(10,2) default 0,
   SALIDA_COMPRA                 decimal(10,2) default 0,
   DESCUENTO_COMPRA         decimal(10,2) default 0,
   TOTAL_COMPRA                 decimal(10,2) default 0,
   SALDO_CREDITO_COMPRA         decimal(10,2) default 0,
   INTERES_CREDITO_COMPRA         decimal(10,2) default 0,
   TOTALMORA_CREDITO_COMPRA         decimal(10,2) default 0,
   ULTIMOPAGO_CREDITO_COMPRA         date,
   FECHAVENC_CREDITO_COMPRA         date,
   ESTADO_CREDITO_COMPRA         varchar(10),
   FECHA_CANCELADO                 date,
   ESTADO_FACTURA varchar(15) default 'PRE-INGRESO',
   NOTA_FACTURA                        text,
   primary key(DOCUMENTO_COMPRA,FECHA_DOCUMENTO)
);



/*==============================================================*/
/* Table: PAGO_COMPRA */
/*==============================================================*/
create table pago_compra
(
   IDPAGO_COMPRA int not null,
   DOCUMENTO_COMPRA                 varchar(20) not null,
   FECHA_DOCUMENTO         date not null,
   IDTRANSAC         int not null,
   ABONO_PAGO_COMPRA decimal(10,2) default 0,
   MORA_PAGO_COMPRA decimal(10,2) default 0,
   INTERES_PAGO_COMPRA decimal(10,2) default 0,
   TOTAL_PAGO_COMPRA decimal(10,2) default 0,
   primary key (IDPAGO_COMPRA)
);



/*==============================================================*/
/* Table: INVENTARIO */
/*==============================================================*/
create table inventario
(
   CODIGO char(20) not null,
   TIPO_PRODUCTO int not null,
   ESTILO varchar(15) not null,
   COLOR varchar(30) not null,
   TALLA varchar(6),
   MARCA varchar(15),
   MATERIAL varchar(20),
   GENERO char(1),
   CLASEPERSONA char(1),
   PROVEEDOR int not null,
   NUMCAJA char(10),
   PROCEDENCIA varchar(10) default 'NACIONAL',
   COSTOUNITARIO decimal(10,2) default 0,
   COSTOREAL decimal(10,2) default 0,
   COSTO_CONTABLE decimal(10,2) default 0,
   PRECIOMAYOREO decimal(10,2) default 0,
   PRECIOUNITARIO decimal(10,2) default 0,
   PRECIOVENDIDO         decimal(10,2) default 0,
   ESTADOPRODUCTO varchar(30) not null,
   NOTA_PRODUCTO text,
   NUMINGRESO int not null,
   FECHA_INGRESO date,
   FECHA_REINGRESO date,
   FECHA_EGRESO date,
   UBICACION_PRODUCTO text,
   MUESTRA_DER char(5),
   MUESTRA_IZQ                char(5),
   primary key (CODIGO)
);



/*==============================================================*/
/* Table: PREINGRESO - TABLA AUXILIAR */
/*==============================================================*/
create table preingreso
(
   CODIGO_PREINGRESO                  int not null,
   DOCUMENTO_COMPRA                  varchar(20) not null,
   FECHA_DOCUMENTO          date not null,
   TIPO_PRODUCTO_PREINGRESO int not null,
   ESTILO_PREINGRESO varchar(15) not null,
   NUMCAJA_PREINGRESO int,
   COLOR_PREINGRESO varchar(30) not null,
   MARCA_PREINGRESO varchar(15),
   MATERIAL_PREINGRESO varchar(20),
   GENERO_PREINGRESO char(1),
   CLASEPERSONA_PREINGRESO char(1),
   TALLA_PREINGRESO varchar(6),
   COSTOUNITARIO_PREINGRESO decimal(10,2) default 0,
   CODIGO_BARRA char(20),
   CODIGO_CAJA char(10),
   RESPONSABLE char(5),
   ESTADO_PREINGRESO varchar(15) default 'CODIFICANDO',
   primary key (CODIGO_PREINGRESO)
);



/*==============================================================*/
/* Table: PEDIDO */
/*==============================================================*/
create table pedido
(
   NUMPEDIDO varchar(6) not null,
   FECHA_PEDIDO date not null,
   FECHA_INGRESO date not null,
   FECHA_ENTREGA date,
   RESPONSABLE_PEDIDO char(5) not null,
   CLIENTE_PEDIDO int not null,
   TIPO_PAGO varchar(10) not null,
   OBSERVACIONES_PEDIDO text,
   TOTAL_PEDIDO decimal(10,2) not null,
   ESTADO_PEDIDO varchar(10) not null,
   TIPO_PRODUCTO         int,
   APROBADO_POR         char(5),
   primary key (NUMPEDIDO, FECHA_PEDIDO)
);


/*==============================================================*/
/* Table: DETALLE_PEDIDO */
/*==============================================================*/
create table detalle_pedido
(
   CORRELATIVO int not null,        
   NUMPEDIDO varchar(6) not null,
   FECHA_PEDIDO date not null,
   TIPO_PRODUCTO int not null,
   CODESTILO varchar(15) not null,
   COLOR varchar(30),
   TALLA varchar(6),
   CANTIDAD int not null default 0,
   PU decimal(10,2) default 0,
   MONTO                decimal(10,2) default 0,
   primary key (CORRELATIVO)
);


/*==============================================================*/
/* Table: ENVIO */
/*==============================================================*/
create table envio
(
   NUMENVIO                 int not null,        
   NUMPEDIDO varchar(6) not null,
   FECHA_PEDIDO date not null,
   FECHA_EMPAQUETADO date not null,
   TOTAL_UNIDADES int not null default 0,
   SUB_TOTAL decimal(10,2) not null default 0,
   VENDEDOR char(5) not null,
   DESPACHO char(5) not null,
   ESTADO                varchar(20),
   NOTA text,
   primary key (NUMENVIO)
);

/*==============================================================*/
/* Table: DETALLE_ENVIO */
/*==============================================================*/
create table detalle_envio
(
   NUMENVIO                 int not null,        
   CODIGO_PRODUCTO char(20) not null,
   PRECIO_FACTURAR decimal(10,2) default 0,
   LINEA_VENTA                int,
   NOTA                        varchar(20),
   CAMBIO_PRODUCTO        char(20),
   primary key (NUMENVIO,CODIGO_PRODUCTO)
);



/*==============================================================*/
/* Table: PRODUCTO */
/*==============================================================*/
create table producto
(
   TIPO_PRODUCTO int not null,
   CODESTILO varchar(15) not null,
   NOMBRE_ESTILO varchar(30) ,
   DETALLE_ESTILO         text,
   PRECIOVENTA_MAYOREO decimal(10,2) default 0,
   PRECIOVENTA_UNIDAD decimal(10,2) default 0,
   PATH_PICTURE                text,
   TIPO_MIME                varchar(20),
   primary key (CODESTILO, TIPO_PRODUCTO)
);

/*==============================================================*/
/* Table: PROVEEDOR */
/*==============================================================*/
create table proveedor
(
   IDPROVEEDOR int not null,
   NOMBRE_PROVEEDOR varchar(50) not null,
   DUI_PROVEEDOR varchar(10),
   NIT_PROVEEDOR varchar(17),
   NRC_PROVEEDOR varchar(25),
   RUBRO_PROVEEDOR varchar(40),
   DIRECCION_PROVEEDOR text,
   PAIS_PROVEEDOR varchar(50),
   TEL_PROVEEDOR varchar(25),
   NOMBRE_CONTACTO varchar(50),
   TEL_CONTACTO varchar(25),
   NOTA_PROVEEDOR text,
   primary key (IDPROVEEDOR)
);

/*==============================================================*/
/* Table: TRANSACCION */
/*==============================================================*/
create table transaccion
(
   IDTRANSAC int not null ,
   RESPONSABLE_TRANSAC char(5) not null,
   TIPO_TRANSAC int not null,
   FECHA_TRANSAC date not null,
   HORA_TRANSAC time,
   primary key (IDTRANSAC)
);

/*==============================================================*/
/* Table: VENTA */
/*==============================================================*/
create table venta
(
   NUMVENTA int not null,
   MONTO_VENTA decimal(10,2) default 0 not null,
   DESCUENTO_VENTA float default 0,
   DEVOLUCIONES_VENTA decimal(10,2) default 0,
   VENTA_NETA         decimal(10,2) default 0,
   TIPO_VENTA varchar(10) not null,
   ESTADO_VENTA varchar(10) not null default 'ACTIVO',
   ORDEN_ENVIO         int not null,
   IDTRANSAC         int not null,
   DESPACHADO_A char(5),
   NOTA_VENTA                text,
   primary key (NUMVENTA)
);


/*==============================================================*/
/* Table: FACTURAS EMITIDAS */
/*==============================================================*/
create table factura
(
   NUMFACTURA                 varchar(10) not null,
   FECHA_FACTURA                 date not null,
   NUMVENTA                        int not null,
   CLIENTE                        int not null,
   TIPO_FACTURA                        varchar(5) not null,
   SUMAS                 decimal(10,2) default 0,
   IVA decimal(10,2) default 0,
   SUB_TOTAL                 decimal(10,2) default 0,
   PORCENTAJE_DESCUENTO             float default 0,
   DESCUENTO                          decimal(10,2) default 0,
   TOTAL                 decimal(10,2) default 0,
   CONDICION_PAGO         varchar(10) not null,
   SALDO                        decimal(10,2) default 0,
   TASA_INTERES                        float default 0,        
   FECHA_PROXIMO_PAGO         date,
   FECHA_VENCIMIENTO         date,
   FECHA_CANCELADO                 date,
   ESTADO                         varchar(10) default 'ACTIVO',
   OBSERVACIONES                text,
   primary key(NUMFACTURA,FECHA_FACTURA)
);


/*==============================================================*/
/* Table: DETALLE FACTURA -- LINEA DE VENTA */
/*==============================================================*/
create table detalle_factura
(
  ID                 int not null,
  NUMFACTURA varchar(10) not null,
  FECHA_FACTURA date not null,
  CANTIDAD         int not null,
  TIPO int not null,
  ESTILO         varchar(15) not null,
  COLORES         varchar(50),
  PU         decimal(10,2) not null,
  MONTO_LINEA decimal(10,2) not null,
  primary key(ID)
);



/*==============================================================*/
/* Table: PAGO_VENTA */
/*==============================================================*/
create table pago_venta
(
   IDPAGO int not null,
   NUMFACTURA         varchar(10) not null,
   FECHA_FACTURA         date not null,
   IDTRANSAC int not null,
   DETALLE_PAGO text,
   ABONO         decimal(10,2) default 0,
   MORA         decimal(10,2) default 0,
   INTERES         decimal(10,2) default 0,
   TOTAL_PAGO decimal(10,2) default 0 not null,
   primary key (IDPAGO)
);

/*==============================================================*/
/* Table: DEVOLUCIONES Y CAMBIOS */
/*==============================================================*/
create table devolucion
(
  NUMDEVOLUCION         int not null,
  NUMFACTURA         varchar(10) not null,
  FECHA_FACTURA         date not null,
  FECHA_DEVOLUCION date not null,
  RESPONSABLE char(5) not null,
  UNIDADES_DEVOLUCION int not null,
  MONTO_DEVOLUCION        decimal(10,2),
  DESCUENTO_DEVOLUCION decimal(10,2),
  TOTAL_DEVOLUCION         decimal(10,2),
  SALDO_DEVOLUCION decimal(10,2),
  ESTADO_DEVOLUCION varchar(20) not null,
  NOTA_DEVOLUCION        text,
  primary key (NUMDEVOLUCION)
);



/*==============================================================*/
/* Table: DEVOLUCIONES--> FACTURAS */
/*==============================================================*/
create table devolucion_facturas
(
  ID int not null,
  DEVOLUCION         int not null,
  FACTURA                 varchar(10) not null,
  FECHA                         date not null,
  primary key (ID)
);



/*==============================================================*/
/* INDICES --> VALORES UNICOS */
/*==============================================================*/

create unique index UI_USUARIO on empleado(USERNAME);
create unique index UI_PROVEEDOR on proveedor(NOMBRE_PROVEEDOR);
create unique index UI_ENVIO_VENTA on venta(ORDEN_ENVIO);







/*==============================================================*/
/* LLAVES FORANEAS --> RELACIONES */
/*==============================================================*/

alter table permisos add constraint FK_EMPLEADO_PERMISOS foreign key (EMPLEADO)
      references empleado(CODEMPLEADO) on delete restrict on update restrict;




alter table cliente add constraint FK_EMPLEADO_CLIENTE foreign key (EMPLEADOASIGNADO)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;

alter table factura_ingreso add constraint FK_POVEEDOR_FACTURA_INGRESO foreign key (PROVEEDOR)
      references proveedor (IDPROVEEDOR) on delete restrict on update restrict;

alter table factura_ingreso add constraint FK_RETACEO_FACTURA_INGRESO foreign key (NUMRETACEO)
      references retaceo(NUMRETACEO)on delete restrict on update restrict;



alter table preingreso add constraint FK_FACTURA_PREINGRESO foreign key (DOCUMENTO_COMPRA,FECHA_DOCUMENTO)
      references factura_ingreso(DOCUMENTO_COMPRA,FECHA_DOCUMENTO) on delete restrict on update restrict;

alter table preingreso add constraint FK_PRODUCTO_PREINGRESO foreign key (ESTILO_PREINGRESO, TIPO_PRODUCTO_PREINGRESO)
      references producto (CODESTILO, TIPO_PRODUCTO) on delete restrict on update restrict;




alter table ingreso add constraint FK_INGRESO_FACTURA foreign key (DOCUMENTO_COMPRA,FECHA_DOCUMENTO)
      references factura_ingreso(DOCUMENTO_COMPRA,FECHA_DOCUMENTO) on delete restrict on update restrict;

alter table ingreso add constraint FK_TRANSACCION_INGRESO foreign key (IDTRANSAC)
      references transaccion (IDTRANSAC) on delete restrict on update restrict;

alter table retaceo add constraint FK_TRANSACCION_RETACEO foreign key (IDTRANSAC)
      references transaccion (IDTRANSAC) on delete restrict on update restrict;


alter table pago_compra add constraint FK_PAGOCOMP_FACTURA foreign key (DOCUMENTO_COMPRA,FECHA_DOCUMENTO)
      references factura_ingreso(DOCUMENTO_COMPRA,FECHA_DOCUMENTO) on delete restrict on update restrict;

alter table pago_compra add constraint FK_TRANSACCION_PAGOCOMPRA foreign key (IDTRANSAC)
      references transaccion (IDTRANSAC) on delete restrict on update restrict;


alter table caja add constraint FK_PRODUCTO_CAJA foreign key (ESTILO, TIPO_PRODUCTO)
      references producto (CODESTILO, TIPO_PRODUCTO) on delete restrict on update restrict;



alter table inventario add constraint FK_INGRESO_INVENTARIO foreign key (NUMINGRESO)
      references ingreso (NUMINGRESO) on delete restrict on update restrict;

alter table inventario add constraint FK_INVENTARIO_CAJA foreign key (NUMCAJA)
      references caja (NUMCAJA) on delete restrict on update restrict;

alter table inventario add constraint FK_PRODUCTO_INVENTARIO foreign key (ESTILO, TIPO_PRODUCTO)
      references producto (CODESTILO, TIPO_PRODUCTO) on delete restrict on update restrict;

alter table inventario add constraint FK_PROVEEDOR_INVENTARIO foreign key (PROVEEDOR)
      references proveedor (IDPROVEEDOR) on delete restrict on update restrict;

alter table inventario add constraint FK_EMPLEADO_MUESTRA_DER foreign key (MUESTRA_DER)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;

alter table inventario add constraint FK_EMPLEADO_MUESTRA_IZQ foreign key (MUESTRA_IZQ)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;



alter table pedido add constraint FK_CLIENTE_PEDIDO foreign key (CLIENTE_PEDIDO)
      references cliente (NUMCUENTA) on delete restrict on update restrict;

alter table pedido add constraint FK_EMPLEADO_PEDIDO foreign key (RESPONSABLE_PEDIDO)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;

alter table pedido add constraint FK_APROBADOPOR_PEDIDO foreign key (APROBADO_POR)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;



alter table detalle_pedido add constraint FK_PEDIDO_DETALLEPEDIDO foreign key (NUMPEDIDO, FECHA_PEDIDO)
      references pedido (NUMPEDIDO, FECHA_PEDIDO) on delete restrict on update restrict;

alter table detalle_pedido add constraint FK_PRODUCTO_DETALLEPEDIDO foreign key (CODESTILO, TIPO_PRODUCTO)
      references producto (CODESTILO, TIPO_PRODUCTO) on delete restrict on update restrict;



alter table transaccion add constraint FK_EMPLEADO_TRANSACCION foreign key (RESPONSABLE_TRANSAC)
      references empleado(CODEMPLEADO) on delete restrict on update restrict;




alter table envio add constraint FK_PEDIDO_ENVIO foreign key (NUMPEDIDO,FECHA_PEDIDO)
      references pedido (NUMPEDIDO,FECHA_PEDIDO) on delete restrict on update restrict;

alter table envio add constraint FK_VENDEDOR_ENVIO foreign key (VENDEDOR)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;

alter table envio add constraint FK_DESPACHO_ENVIO foreign key (DESPACHO)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;




alter table detalle_envio add constraint FK_ENVIO_DETALLE foreign key (NUMENVIO)
      references envio (NUMENVIO) on delete restrict on update restrict;

alter table detalle_envio add constraint FK_PRODUCTO_DETALLE foreign key (CODIGO_PRODUCTO)
      references inventario (CODIGO) on delete restrict on update restrict;

alter table detalle_envio add constraint FK_DETALLES_FACTURA_ENVIO foreign key (LINEA_VENTA)
      references detalle_factura(ID) on delete restrict on update restrict;

alter table detalle_envio add constraint FK_CAMBIO_DETALLE foreign key (CAMBIO_PRODUCTO)
      references inventario (CODIGO) on delete restrict on update restrict;




alter table venta add constraint FK_ENVIO_VENTA foreign key (ORDEN_ENVIO)
      references envio (NUMENVIO) on delete restrict on update restrict;

alter table venta add constraint FK_TRANSACCION_VENTA foreign key (IDTRANSAC)
      references transaccion (IDTRANSAC) on delete restrict on update restrict;

alter table venta add constraint FK_VENDEDOR_VENTA foreign key (DESPACHADO_A)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;




alter table factura add constraint FK_VENTA_FACTURA foreign key (NUMVENTA)
      references venta(NUMVENTA) on delete restrict on update restrict;

alter table factura add constraint FK_CLIENTE_FACTURA foreign key (CLIENTE)
      references cliente(NUMCUENTA) on delete restrict on update restrict;



alter table detalle_factura add constraint FK_DETALLE_FACTURA foreign key (NUMFACTURA,FECHA_FACTURA)
      references factura(NUMFACTURA,FECHA_FACTURA) on delete restrict on update restrict;






alter table pago_venta add constraint FK_TRANSACCION_PAGO foreign key (IDTRANSAC)
      references transaccion (IDTRANSAC) on delete restrict on update restrict;

alter table pago_venta add constraint FK_FACTURA_PAGO foreign key (NUMFACTURA,FECHA_FACTURA)
      references factura(NUMFACTURA,FECHA_FACTURA) on delete restrict on update restrict;





alter table devolucion add constraint FK_FACTURA_DEVOLUCION foreign key (NUMFACTURA,FECHA_FACTURA)
      references factura(NUMFACTURA,FECHA_FACTURA) on delete restrict on update restrict;

alter table devolucion add constraint FK_RESPONSABLE_DEVOLUCION foreign key (RESPONSABLE)
      references empleado (CODEMPLEADO) on delete restrict on update restrict;






alter table devolucion_facturas add constraint FK_FACTURAS_DEVOLUCION foreign key (DEVOLUCION)
      references devolucion (NUMDEVOLUCION) on delete restrict on update restrict;

alter table devolucion_facturas add constraint FK_DEVOLUCION_FACTURAS foreign key (FACTURA,FECHA)
      references factura(NUMFACTURA,FECHA_FACTURA) on delete restrict on update restrict;

/*==============================================================*/
/* FUNCIONES */
/*==============================================================*/







/*==============================================================*/
/* PROCEDIMIENTOS */
/*==============================================================*/







/*==============================================================*/
/* TRIGGERS */
/*==============================================================*/

