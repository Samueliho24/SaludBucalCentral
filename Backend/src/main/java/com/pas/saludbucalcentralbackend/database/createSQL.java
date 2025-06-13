
package com.pas.saludbucalcentralbackend.database;

/**
 *
 * @author Sistemas
 */
public class createSQL {
    private static String usuarios = "CREATE TABLE IF NOT EXISTS usuarios(\n" +
"	cedula integer primary key,\n" +
"	nombre text,\n" +
"	password text,\n" +
"	tipo text,\n" +
"	create_at datetime default CURRENT_TIMESTAMP,\n" +
"	estado text default 'Activo'\n" +
");";
    private static String formularios = "CREATE TABLE IF NOT EXISTS formularios(\n" +
"	fecha datetime default CURRENT_TIMESTAMP,\n" +
"	examinador text not null,\n" +
"	examinador_cedula integer not null,\n" +
"	cedula integer not null,\n" +
"	nombre text not null,\n" +
"	apellido text not null,\n" +
"	genero text not null,\n" +
"	edad integer not null,\n" +
"	estado text not null,\n" +
"	etnia text not null,\n" +
"	etnia_zulia text,\n" +
"	municipio text not null,\n" +
"	parroquia text not null,\n" +
"	temporomaxilar_sintoma char default '8',\n" +
"	temporomaxilar_signo char default '8',\n" +
"	lesion char default '8',\n" +
"	lesion_localizacion char default '8',\n" +
"	lesion_dolor char default '8',\n" +
"	lesion_color char default '8',\n" +
"	lesion_tamaño char default '8',\n" +
"	lesion_consistencia char default '8',\n" +
"	lesion_morfologia char default '8',\n" +
"	lesion_numero char default '8',\n" +
"	flurosis char default '8',\n" +
"	placa_16V char default '8',\n" +
"	placa_11V char default '8',\n" +
"	placa_26V char default '8',\n" +
"	placa_36L char default '8',\n" +
"	placa_31V char default '8',\n" +
"	placa_46L char default '8',\n" +
"	calculo_16V char default '8',\n" +
"	calculo_11V char default '8',\n" +
"	calculo_26V char default '8',\n" +
"	calculo_36L char default '8',\n" +
"	calculo_31V char default '8',\n" +
"	calculo_46L char default '8',\n" +
"	peridontico_0 char default '8',\n" +
"	peridontico_1 char default '8',\n" +
"	peridontico_2 char default '8',\n" +
"	peridontico_3 char default '8',\n" +
"	peridontico_4 char default '8',\n" +
"	peridontico_5 char default '8',\n" +
"	corona_17 char not null,\n" +
"	corona_16 char not null,\n" +
"	corona_15 char not null,\n" +
"	corona_14 char not null,\n" +
"	corona_13 char not null,\n" +
"	corona_12 char not null,\n" +
"	corona_11 char not null,\n" +
"	tratamiento_17 char not null,\n" +
"	tratamiento_16 char not null,\n" +
"	tratamiento_15 char not null,\n" +
"	tratamiento_14 char not null,\n" +
"	tratamiento_13 char not null,\n" +
"	tratamiento_12 char not null,\n" +
"	tratamiento_11 char not null,\n" +
"	corona_21 char not null,\n" +
"	corona_22 char not null,\n" +
"	corona_23 char not null,\n" +
"	corona_24 char not null,\n" +
"	corona_25 char not null,\n" +
"	corona_26 char not null,\n" +
"	corona_27 char not null,\n" +
"	tratamiento_21 char not null,\n" +
"	tratamiento_22 char not null,\n" +
"	tratamiento_23 char not null,\n" +
"	tratamiento_24 char not null,\n" +
"	tratamiento_25 char not null,\n" +
"	tratamiento_26 char not null,\n" +
"	tratamiento_27 char not null,\n" +
"	corona_47 char not null,\n" +
"	corona_46 char not null,\n" +
"	corona_45 char not null,\n" +
"	corona_44 char not null,\n" +
"	corona_43 char not null,\n" +
"	corona_42 char not null,\n" +
"	corona_41 char not null,\n" +
"	tratamiento_47 char not null,\n" +
"	tratamiento_46 char not null,\n" +
"	tratamiento_45 char not null,\n" +
"	tratamiento_44 char not null,\n" +
"	tratamiento_43 char not null,\n" +
"	tratamiento_42 char not null,\n" +
"	tratamiento_41 char not null,\n" +
"	corona_31 char not null,\n" +
"	corona_32 char not null,\n" +
"	corona_33 char not null,\n" +
"	corona_34 char not null,\n" +
"	corona_35 char not null,\n" +
"	corona_36 char not null,\n" +
"	corona_37 char not null,\n" +
"	tratamiento_31 char not null,\n" +
"	tratamiento_32 char not null,\n" +
"	tratamiento_33 char not null,\n" +
"	tratamiento_34 char not null,\n" +
"	tratamiento_35 char not null,\n" +
"	tratamiento_36 char not null,\n" +
"	tratamiento_37 char not null,\n" +
"	erupcion_dentaria_17 char default '8',\n" +
"	erupcion_dentaria_16 char default '8',\n" +
"	erupcion_dentaria_15 char default '8',\n" +
"	erupcion_dentaria_14 char default '8',\n" +
"	erupcion_dentaria_13 char default '8',\n" +
"	erupcion_dentaria_12 char default '8',\n" +
"	erupcion_dentaria_11 char default '8',\n" +
"	erupcion_dentaria_21 char default '8',\n" +
"	erupcion_dentaria_22 char default '8',\n" +
"	erupcion_dentaria_23 char default '8',\n" +
"	erupcion_dentaria_24 char default '8',\n" +
"	erupcion_dentaria_25 char default '8',\n" +
"	erupcion_dentaria_26 char default '8',\n" +
"	erupcion_dentaria_27 char default '8',\n" +
"	erupcion_dentaria_47 char default '8',\n" +
"	erupcion_dentaria_46 char default '8',\n" +
"	erupcion_dentaria_45 char default '8',\n" +
"	erupcion_dentaria_44 char default '8',\n" +
"	erupcion_dentaria_43 char default '8',\n" +
"	erupcion_dentaria_42 char default '8',\n" +
"	erupcion_dentaria_41 char default '8',\n" +
"	erupcion_dentaria_31 char default '8',\n" +
"	erupcion_dentaria_32 char default '8',\n" +
"	erupcion_dentaria_33 char default '8',\n" +
"	erupcion_dentaria_34 char default '8',\n" +
"	erupcion_dentaria_35 char default '8',\n" +
"	erupcion_dentaria_36 char default '8',\n" +
"	erupcion_dentaria_37 char default '8',\n" +
"	protesis_necesidad_superior char default '8',\n" +
"	protesis_necesidad_inferior char default '8',\n" +
"	protesis_tipo_superior char default '8',\n" +
"	protesis_tipo_inferior char default '8',\n" +
"	habitos_bruxismo char default '8',\n" +
"	habitos_respiracion_bucal char default '8',\n" +
"	habitos_deglucion_atipica char default '8',\n" +
"	habitos_succion_digital char default '8',\n" +
"	habitos_ninguno char default '8',\n" +
"	relacion_oclusion_2dos_molares char default '8',\n" +
"	relacion_oclusion_1ros_molares char default '8',\n" +
"	apiñamiento char not null,\n" +
"	espaciamiento char not null,\n" +
"	diastema char not null,\n" +
"	mordida_abierta char not null,\n" +
"	morida_profunda_anterior char not null,\n" +
"	morida_tope_a_tope char not null,\n" +
"	mordida_cruzada char not null,\n" +
"	necesidad_asistencia_trastorno char not null,\n" +
"	necesidad_asistencia_dolor char not null,\n" +
"	\n" +
"	exportado int default 0\n" +
");";

    public static String getUsuarios() {
        return usuarios;
    }

    public static String getFormularios() {
        return formularios;
    }
    
    
}
