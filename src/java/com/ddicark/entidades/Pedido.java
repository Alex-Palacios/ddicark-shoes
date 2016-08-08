/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Pedido.findPedido", query = "SELECT p FROM Pedido p WHERE p.pedidoPK = :pedidoPK"),
    @NamedQuery(name = "Pedido.findByTipoPago", query = "SELECT p FROM Pedido p WHERE p.tipoPago = :tipoPago ORDER BY p.fechaEntrega"),
    @NamedQuery(name = "Pedido.findByEstadoPedido", query = "SELECT p FROM Pedido p WHERE p.estadoPedido = :estadoPedido ORDER BY p.fechaEntrega"),
    @NamedQuery(name = "Pedido.enCola", query = "SELECT p FROM Pedido p WHERE p.estadoPedido IN ('APROBADO','PROCESANDO') ORDER BY p.fechaEntrega"),
    @NamedQuery(name = "Pedido.solicitudCreditos", query = "SELECT p FROM Pedido p WHERE p.tipoPago = 'AL CREDITO' AND p.estadoPedido = 'SOLICITADO' ORDER BY p.pedidoPK.fechaPedido"),
    @NamedQuery(name = "Pedido.byMonthYear", query = "SELECT p FROM Pedido p WHERE FUNC('YEAR',p.pedidoPK.fechaPedido) = :year AND  FUNC('MONTH',p.pedidoPK.fechaPedido) = :month ") })
public class Pedido implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidoPK pedidoPK;
    @Column(name = "FECHA_ENTREGA")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO_PAGO")
    private String tipoPago;
    @Lob
    @Size(max = 65535)
    @Column(name = "OBSERVACIONES_PEDIDO")
    private String observacionesPedido;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_PEDIDO")
    private BigDecimal totalPedido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ESTADO_PEDIDO")
    private String estadoPedido;
    @Basic(optional = false)
    @Column(name = "TIPO_PRODUCTO")
    private Integer tipoProducto;
    @JoinColumn(name = "CLIENTE_PEDIDO", referencedColumnName = "NUMCUENTA")
    @ManyToOne(optional = false)
    private Cliente clientePedido;
    @JoinColumn(name = "RESPONSABLE_PEDIDO", referencedColumnName = "CODEMPLEADO")
    @ManyToOne(optional = false)
    private Empleado responsablePedido;
    @JoinColumn(name = "APROBADO_POR", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado aprobadoPor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<DetallePedido> detallePedidoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<Envio> envio;
    
    public Pedido() {
    }

    public Pedido(PedidoPK pedidoPK) {
        this.pedidoPK = pedidoPK;
    }

    public Pedido(PedidoPK pedidoPK,Date fechaIngreso, String tipoPago, BigDecimal totalPedido, String estadoPedido) {
        this.pedidoPK = pedidoPK;
        this.fechaIngreso = fechaIngreso;
        this.tipoPago = tipoPago;
        this.totalPedido = totalPedido;
        this.estadoPedido = estadoPedido;
    }

    public Pedido(String numpedido, Date fechaPedido) {
        this.pedidoPK = new PedidoPK(numpedido, fechaPedido);
    }

    public PedidoPK getPedidoPK() {
        return pedidoPK;
    }

    public void setPedidoPK(PedidoPK pedidoPK) {
        this.pedidoPK = pedidoPK;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getObservacionesPedido() {
        return observacionesPedido;
    }

    public void setObservacionesPedido(String observacionesPedido) {
        this.observacionesPedido = observacionesPedido;
    }

    public BigDecimal getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(BigDecimal totalPedido) {
        this.totalPedido = totalPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Integer getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(Integer tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

   
    
    @XmlTransient
    public Collection<DetallePedido> getDetallePedidoCollection() {
        return detallePedidoCollection;
    }
    
    public void setDetallePedidoCollection(Collection<DetallePedido> detallePedidoCollection) {
        this.detallePedidoCollection = detallePedidoCollection;
    }

    @XmlTransient
    public Collection<Envio> getEnvio() {
        return envio;
    }

    public void setEnvio(Collection<Envio> envio) {
        this.envio = envio;
    }
    
    

    public Cliente getClientePedido() {
        return clientePedido;
    }

    public void setClientePedido(Cliente clientePedido) {
        this.clientePedido = clientePedido;
    }

    public Empleado getResponsablePedido() {
        return responsablePedido;
    }

    public void setResponsablePedido(Empleado responsablePedido) {
        this.responsablePedido = responsablePedido;
    }

    public Empleado getAprobadoPor() {
        return aprobadoPor;
    }

    public void setAprobadoPor(Empleado aprobadoPor) {
        this.aprobadoPor = aprobadoPor;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoPK != null ? pedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.pedidoPK == null && other.pedidoPK != null) || (this.pedidoPK != null && !this.pedidoPK.equals(other.pedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Pedido[ pedidoPK=" + pedidoPK + " ]";
    }

    
    
}
