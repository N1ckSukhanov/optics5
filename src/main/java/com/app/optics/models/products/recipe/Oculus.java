package com.app.optics.models.products.recipe;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.app.optics.util.ObjectConverter.*;

@Entity
@Table(name = "oculus")
@Data
@NoArgsConstructor
public class Oculus {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Recipe recipe;
    @Column(name = "od_sph")
    private Double odSph;
    @Column(name = "os_sph")
    private Double osSph;
    @Column(name = "od_cyl")
    private Double odCyl;
    @Column(name = "os_cyl")
    private Double osCyl;
    @Column(name = "od_ax")
    private Integer odAx;
    @Column(name = "os_ax")
    private Integer osAx;
    @Column(name = "od_add")
    private Double odAdd;
    @Column(name = "os_add")
    private Double osAdd;

    public Oculus(Double odSph, Double osSph, Double odCyl, Double osCyl, Integer odAx, Integer osAx, Double odAdd, Double osAdd) {
        this.odSph = odSph;
        this.osSph = osSph;
        this.odCyl = odCyl;
        this.osCyl = osCyl;
        this.odAx = odAx;
        this.osAx = osAx;
        this.odAdd = odAdd;
        this.osAdd = osAdd;
    }

    private String addPlus(String str) {
        if (str == null || str.isBlank())
            return str;

        if (str.charAt(0) != '-')
            return "+" + str;
        return str;
    }

    @Override
    public String toString() {
        return strNoLine("OD sph", addPlus(round(odSph, 2))) +
                strNoLine(" cyl", addPlus(round(odCyl, 2))) +
                strNoLine(" ax", odAx) +
                str(" add", round(odAdd, 2)) +
                strNoLine("OS sph", addPlus(round(osSph, 2))) +
                strNoLine(" cyl", addPlus(round(osCyl, 2))) +
                strNoLine(" ax", osAx) +
                str(" add", round(osAdd, 2));
    }
}
