package org.abner.manager.model.sms;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.movimento.Movimento;

public class Sms extends AbstractModel {

    private static final long serialVersionUID = -3167881552240918565L;

    private Movimento movimento;

    private Date dateSent;

    private String body;

    private String address;

    private String serviceCenter;

    private Boolean debito;

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BigDecimal findValue() {
        if (body != null) {
            NumberFormat format = DecimalFormat.getInstance();
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);

            List<Double> values = new ArrayList<Double>();
            for (String part : body.split("\\s")) {
                if (part.matches("[0-9.,]+")) {
                    if (part.length() >= 4
                                    && part.substring(0, 1).matches("[0-9]")
                                    && part.substring(part.length() - 2).matches("[0-9]{2}")
                                    && part.substring(part.length() - 3, part.length() - 2).matches("[.,]")) {
                        try {
                            Number value = format.parse(part);
                            values.add(value.doubleValue());
                        } catch (ParseException ignored) {}
                    }
                }
            }
            if (values.size() == 1) {
                return new BigDecimal(values.get(0));
            }
        }
        return null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public Boolean getDebito() {
        return debito;
    }

    public void setDebito(Boolean debito) {
        this.debito = debito;
    }

}
