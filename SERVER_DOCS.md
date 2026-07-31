# Documentación del Formulario — Evaluación de Salud Bucal (Cliente → Servidor)

Este documento describe la **estructura completa del formulario**, los **estilos CSS** aplicados, el **schema de base de datos**, y el **protocolo de comunicación** entre el cliente Android (WebView) y el servidor MySQL.

---

## 1. Arquitectura General

```
┌─────────────────────────────┐         JSON POST          ┌─────────────────────┐
│  Cliente Android (WebView)  │ ─────────────────────────▶ │  Servidor MySQL     │
│  HTML/CSS/JS + SQLite       │ ◀───────────────────────── │  (API HTTP :8080)   │
│                             │     HTTP 200 / syncUsers   │                     │
└─────────────────────────────┘                            └─────────────────────┘
```

- **Cliente**: App Android con WebView. Los formularios son HTML embebidos. JS captura los datos y los envía como JSON via `Android.post(method, json)`.
- **Servidor**: MySQL en la red local de LUZ (IP configurable via código XOR). Expone endpoints HTTP en puerto `8080`.
- **No hay framework**: Vanilla HTML/CSS/JS. Sin npm, bundler, ni dependencias.

---

## 2. Endpoints del Servidor

| Método | URL | Body | Descripción |
|--------|-----|------|-------------|
| POST | `/recieverData` | `JSONArray` de objetos con todos los campos del formulario | Recibe formularios exportados (salud bucal + socioeconómico). Cada objeto tiene un campo `_table` si es de la tabla `socioeconomico`. Incluye campo `password` para autenticación. |
| GET | `/syncUsers` | — | Devuelve `{ "dataUsers": [ { "cedula": int, "nombre": str, "password": str }, ... ] }`. El cliente reemplaza toda la tabla `usuarios` local con esta respuesta. |

### Formato del JSON enviado en `/recieverData`

```json
[
  {
    "_table": "socioeconomico",
    "examinador": "Nombre del Examinador",
    "examinador_cedula": "12345678",
    "password": "sha1_hash_del_usuario",
    "cedula": "12345678",
    "nombre": "Juan",
    "apellido": "Pérez",
    "genero": "1",
    "edad": "65",
    "estado": "Zulia",
    "municipio": "Maracaibo",
    "parroquia": "Cacique Mara"
  }
]
```

> **Nota**: Todos los valores son strings. Los campos `examinador`, `examinador_cedula` y `password` se agregan automáticamente por el cliente Java. El campo `_table` solo aparece en registros de la tabla `socioeconomico`; si no está presente, el registro pertenece a `formularios`.

---

## 3. Schema de Base de Datos (schema.sql)

### 3.1 Tabla `formularios` — Salud Bucal

```sql
create table formularios(
    -- Metadatos (agregados por el cliente Java, no por el usuario)
    fecha datetime default CURRENT_TIMESTAMP,
    examinador text not null,
    examinador_cedula integer not null,

    -- Datos del paciente
    cedula integer not null,
    nombre text not null,
    apellido text not null,
    genero text not null,
    edad integer not null,
    estado text not null,
    etnia text not null,
    etnia_zulia text,
    municipio text not null,
    parroquia text not null,

    -- Articulación Temporomandibular
    temporomaxilar_sintoma char default '8',
    temporomaxilar_signo char default '8',

    -- Lesión Intraoral
    lesion char default '8',
    lesion_localizacion char default '8',
    lesion_dolor char default '8',
    lesion_color char default '8',
    lesion_tamaño char default '8',
    lesion_consistencia char default '8',
    lesion_morfologia char default '8',
    lesion_numero char default '8',

    -- Fluorosis
    fluorosis char default '8',

    -- Higiene Bucal Simplificada (IHB-S) — 6 dientes × 2 tipos
    placa_16V char default '8', placa_11V char default '8', placa_26V char default '8',
    placa_36L char default '8', placa_31V char default '8', placa_46L char default '8',
    calculo_16V char default '8', calculo_11V char default '8', calculo_26V char default '8',
    calculo_36L char default '8', calculo_31V char default '8', calculo_46L char default '8',

    -- Índice Periodontal Comunitario (IPC) — 6 sextantes
    periodontico_0 char default '8', periodontico_1 char default '8',
    periodontico_2 char default '8', periodontico_3 char default '8',
    periodontico_4 char default '8', periodontico_5 char default '8',

    -- Estado de Dentición (corona) — 28 dientes permanentes
    corona_17 char not null, corona_16 char not null, corona_15 char not null,
    corona_14 char not null, corona_13 char not null, corona_12 char not null, corona_11 char not null,
    corona_21 char not null, corona_22 char not null, corona_23 char not null,
    corona_24 char not null, corona_25 char not null, corona_26 char not null, corona_27 char not null,
    corona_47 char not null, corona_46 char not null, corona_45 char not null,
    corona_44 char not null, corona_43 char not null, corona_42 char not null, corona_41 char not null,
    corona_31 char not null, corona_32 char not null, corona_33 char not null,
    corona_34 char not null, corona_35 char not null, corona_36 char not null, corona_37 char not null,

    -- Tratamiento Necesario — 28 dientes permanentes
    tratamiento_17 char not null, tratamiento_16 char not null, tratamiento_15 char not null,
    tratamiento_14 char not null, tratamiento_13 char not null, tratamiento_12 char not null, tratamiento_11 char not null,
    tratamiento_21 char not null, tratamiento_22 char not null, tratamiento_23 char not null,
    tratamiento_24 char not null, tratamiento_25 char not null, tratamiento_26 char not null, tratamiento_27 char not null,
    tratamiento_47 char not null, tratamiento_46 char not null, tratamiento_45 char not null,
    tratamiento_44 char not null, tratamiento_43 char not null, tratamiento_42 char not null, tratamiento_41 char not null,
    tratamiento_31 char not null, tratamiento_32 char not null, tratamiento_33 char not null,
    tratamiento_34 char not null, tratamiento_35 char not null, tratamiento_36 char not null, tratamiento_37 char not null,

    -- Erupción Dentaria — 28 dientes permanentes
    erupcion_dentaria_17 char default '8', erupcion_dentaria_16 char default '8', erupcion_dentaria_15 char default '8',
    erupcion_dentaria_14 char default '8', erupcion_dentaria_13 char default '8', erupcion_dentaria_12 char default '8',
    erupcion_dentaria_11 char default '8',
    erupcion_dentaria_21 char default '8', erupcion_dentaria_22 char default '8', erupcion_dentaria_23 char default '8',
    erupcion_dentaria_24 char default '8', erupcion_dentaria_25 char default '8', erupcion_dentaria_26 char default '8',
    erupcion_dentaria_27 char default '8',
    erupcion_dentaria_47 char default '8', erupcion_dentaria_46 char default '8', erupcion_dentaria_45 char default '8',
    erupcion_dentaria_44 char default '8', erupcion_dentaria_43 char default '8', erupcion_dentaria_42 char default '8',
    erupcion_dentaria_41 char default '8',
    erupcion_dentaria_31 char default '8', erupcion_dentaria_32 char default '8', erupcion_dentaria_33 char default '8',
    erupcion_dentaria_34 char default '8', erupcion_dentaria_35 char default '8', erupcion_dentaria_36 char default '8',
    erupcion_dentaria_37 char default '8',

    -- Prótesis
    protesis_necesidad_superior char default '8',
    protesis_necesidad_inferior char default '8',
    protesis_tipo_superior char default '8',
    protesis_tipo_inferior char default '8',

    -- Hábitos
    habitos_bruxismo char default '8',
    habitos_respiracion_bucal char default '8',
    habitos_deglucion_atipica char default '8',
    habitos_succion_digital char default '8',
    habitos_ninguno char default '8',

    -- Oclusión y Maloclusión
    relacion_oclusion_2dos_molares char default '8',
    relacion_oclusion_1ros_molares char default '8',
    apiñamiento char not null,
    espaciamiento char not null,
    diastema char not null,
    mordida_abierta char not null,
    mordida_profunda_anterior char not null,
    mordida_tope_a_tope char not null,
    mordida_cruzada char not null,

    -- Necesidad Inmediata de Asistencia
    necesidad_asistencia_trastorno char not null,
    necesidad_asistencia_dolor char not null,

    -- Control de exportación
    exportado int default 0
);
```

### 3.2 Tabla `socioeconomico`

```sql
create table socioeconomico(
    fecha datetime default CURRENT_TIMESTAMP,
    examinador text not null,
    examinador_cedula integer not null,
    cedula integer not null,
    nombre text not null,
    apellido text not null,
    genero text not null,
    edad integer not null,
    autoidentificacion_etnica text not null,
    etnia_indigena text,
    etnia_otro text,
    discapacidad_funcional text not null,
    nivel_independencia text not null,
    tiene_familiares text not null,
    frecuencia_visitas text not null,
    ingresos_pension text not null,
    ingresos_jubilacion text not null,
    ingresos_familiares text not null,
    ingresos_bonos text not null,
    ingresos_ninguno text not null,
    ingresos_otro text not null,
    ingresos_otro_text text,
    necesidades_cubiertas text not null,
    frecuencia_cepillado text not null,
    quien_higiene text not null,
    producto_cepillo text not null,
    producto_crema text not null,
    producto_enjuague text not null,
    producto_hilo text not null,
    producto_protesis text not null,
    producto_ninguno text not null,
    usa_protesis text not null,
    desde_cuando_protesis text,
    como_adquirio_protesis text,
    limpia_boca_protesis text,
    limpia_dentadura_protesis text,
    protesis_dolor text,
    protesis_mueven text,
    protesis_rota text,
    protesis_otra_molestia text,
    protesis_otra_molestia_text text,
    dolor_bucal text not null,
    encias_sangran text not null,
    ultima_vez_odontologo text not null,
    es_fumador text not null,
    cantidad_cigarrillos text,
    consumo_alcohol text not null,
    frecuencia_alcohol text,
    exportado int default 0
);
```

### 3.3 Tabla `usuarios`

```sql
create table usuarios(
    cedula integer primary key,
    nombre text,
    password text,       -- SHA-1 hash
    estado text,
    tipo text
);
```

### 3.4 Tabla `configuracion`

```sql
create table configuracion(
    code text            -- Código cifrado XOR de la IP del servidor
);
```

### 3.5 Trigger `habitos_ninguno`

```sql
create trigger habitos_ninguno_set after insert on formularios
for each row
begin
    update formularios set habitos_ninguno = 1 where
        fecha = new.fecha and
        habitos_bruxismo = 0 and
        habitos_respiracion_bucal = 0 and
        habitos_deglucion_atipica = 0 and
        habitos_succion_digital = 0;
    update formularios set habitos_ninguno = 0 where
        fecha = new.fecha and (
            habitos_bruxismo != 0 or
            habitos_respiracion_bucal != 0 or
            habitos_deglucion_atipica != 0 or
            habitos_succion_digital != 0
        );
end;
```

---

## 4. Estructura del Formulario — Salud Bucal (`form.jinja.html` → `form.html`)

### 4.1 Flujo de Datos

```
form.jinja.html (Jinja2 template)
        │
        ▼  python3 webview/gen.py
    form.html (HTML generado, incluido en APK)
        │
        ▼  post.js intercepta onsubmit
    Android.post("form", JSON)
        │
        ▼  Main.java Handler.post()
    INSERT INTO formularios (...)
        │
        ▼  Android.export()
    POST /recieverData → Servidor MySQL
```

### 4.2 Secciones del Formulario

#### Sección 1: Datos del Paciente

| Campo | Tipo HTML | Nombre del Campo | Valores / Notas |
|-------|-----------|-----------------|-----------------|
| Cédula | `input[type=number]` | `cedula` | `pattern="[1-9][0-9]+"`, requerido |
| Nombre | `input[text]` | `nombre` | requerido |
| Apellido | `input[text]` | `apellido` | requerido |
| Género | `select` | `genero` | `1`=Masculino, `2`=Femenino |
| Edad | `input[type=number]` | `edad` | `pattern="[1-9][0-9]*"`, requerido. Controla campos condicionales por rango de edad |
| Etnia | `select` | `etnia` | `1`=Criollo, `2`=Afrodescendiente, `3`=Indígena |
| Etnia del Zulia | `select` | `etnia_zulia` | `1`=Wayúu, `2`=Añú, `3`=Yukpa, `4`=Japreria, `5`=Barí, `6`=Otro. Solo visible si Etnia=3 y Estado=Zulia |
| Estado | `select` | `estado` | 24 estados venezolanos |
| Municipio | `select`/`input` | `municipio` | Si Estado=Zulia: select con 21 municipios. Si no: input libre |
| Parroquia | `select`/`input` | `parroquia` | Si Estado=Zulia: select con ~80 parroquias. Si no: input libre |

#### Sección 2: Evaluación Extraoral — Articulación Temporomandibular

> Aplicable a partir de los **12 años** (controlado por JS).

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Síntomas | `select` | `temporomaxilar_sintoma` | `0`=Ausencia, `1`=Chasquido articular, `2`=Dolor a la palpación muscular, `3`=Dificultad a la apertura y cierre, `4`=Dolor espontáneo articular, `5`=Dolor a la palpación articular, `6`=Dolor al movimiento |
| Signos | `select` | `temporomaxilar_signo` | `0`=Ausencia, `1`=Chasquido, `2`=Contractura maseteriana, `3`=Reducción de la apertura < 30 mm, `4`=Reducción movimiento de lateralidad y protrusión |

#### Sección 3: Evaluación Intraoral

##### 3.1 Lesión

> Aplicable a partir de los **5 años**. Si "Hay Lesión"=No (`1`), todos los campos se deshabilitan dinámicamente y se les asigna el valor "8 - No Aplica".

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Hay Lesión | `select` | `lesion` | `0`=Si, `1`=No |
| Localización | `select` | `lesion_localizacion` | `0`=Borde bermelión, `1`=Comisuras, `2`=Labios, `3`=Surcos, `4`=Mucosa bucal, `5`=Piso de boca, `6`=Lengua, `7`=Paladar duro y/o blando, `9`=Bordes alveolares |
| Dolor | `select` | `lesion_dolor` | `0`=Si, `1`=No |
| Color | `select` | `lesion_color` | `0`=Normal, `1`=Azul-Violeta, `2`=Negro-Pardo, `3`=Rojo, `4`=Blanca |
| Tamaño | `select` | `lesion_tamaño` | `0`=0-2mm, `1`=3-5mm, `2`=Más de 5mm |
| Consistencia | `select` | `lesion_consistencia` | `0`=Blanda, `1`=Dura, `2`=Resil |
| Morfología | `select` | `lesion_morfologia` | `0`=Plana, `1`=Elevada, `2`=Vesicular, `3`=Úlcera |
| Número | `select` | `lesion_numero` | `0`=Única, `1`=Múltiples iguales, `2`=Múltiples diferentes |

##### 3.2 Fluorosis Dental

> Aplicable a partir de los **6 años**.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Fluorosis | `select` | `fluorosis` | `0`=Normal, `1`=Dudosa, `2`=Muy leve, `3`=Leve, `4`=Moderada, `5`=Severa |

##### 3.3 Índice de Higiene Bucal Simplificado (IHB-S)

6 dientes evaluados en 2 tipos (Placa y Cálculo):

| Diente | Superficie |
|--------|-----------|
| 16 | Vestibular (V) |
| 11 | Vestibular (V) |
| 26 | Vestibular (V) |
| 36 | Lingual (L) |
| 31 | Vestibular (V) |
| 46 | Lingual (L) |

Valores para cada campo: `0`=Ausencia, `1`=Hasta 1/3, `2`=Más de 1/3 hasta 2/3, `3`=Más de 2/3

Nombres de campo: `placa_16V`, `placa_11V`, `placa_26V`, `placa_36L`, `placa_31V`, `placa_46L`, `calculo_16V`, `calculo_11V`, `calculo_26V`, `calculo_36L`, `calculo_31V`, `calculo_46L`

##### 3.4 Índice Periodontal de la Comunidad (IPC)

> Aplicable a partir de los **15 años** (las opciones 3 y 4 se ocultan para menores).

Tabla de 3 columnas × 2 filas con los dientes:
- Superior: 17/16, 11, 26/27
- Inferior: 47/46, 31, 36/37

Valores:
| Código | Significado |
|--------|------------|
| `0` | Sano |
| `1` | Hemorragia |
| `2` | Cálculo |
| `3` | Bolsa de 4-5 mm (Banda negra parcialmente visible) |
| `4` | Bolsa de 6 mm o más (Banda negra invisible) |
| `5` | Sextante excluido (-2 dientes) |
| `9` | Edentulo |

Nombres de campo: `periodontico_0` a `periodontico_5`

##### 3.5 Estado de Dentición y Tratamiento Necesario

Cuatro cuadrantes dentales, cada uno con filas de **Corona** y **Tratamiento**:

| Cuadrante | Dientes | Dirección Visual |
|-----------|---------|-----------------|
| Cuadrante 1 (Superior Derecho) | 17, 16, 15, 14, 13, 12, 11 | Derecha → Izquierda |
| Cuadrante 2 (Superior Izquierdo) | 21, 22, 23, 24, 25, 26, 27 | Izquierda → Derecha |
| Cuadrante 4 (Inferior Derecho) | 47, 46, 45, 44, 43, 42, 41 | Derecha → Izquierda |
| Cuadrante 3 (Inferior Izquierdo) | 31, 32, 33, 34, 35, 36, 37 | Izquierda → Derecha |

**Valores de Corona** (`corona_NN`):
| Código | Significado |
|--------|------------|
| `0` | Sano |
| `A` | Sano (Primario) |
| `1` | Cariado |
| `B` | Cariado (Primario) |
| `2` | Obturado con caries |
| `C` | Obturado con caries (Primario) |
| `3` | Obturado sin caries |
| `D` | Obturado sin caries (Primario) |
| `4` | Perdido por caries |
| `E` | Perdido por caries (Primario) |
| `5` | Perdido por otro motivo |
| `6` | Fisura obturada |
| `F` | Fisura obturada (Primario) |
| `7` | Soporte de puente, corona, implantes |
| `G` | Soporte de puente, corona, implantes (Primario) |
| `8` | Diente sin brotar |
| `T` | Traumatismo (fractura) |

**Valores de Tratamiento** (`tratamiento_NN`):
| Código | Significado |
|--------|------------|
| `0` | Ninguno |
| `P` | Preventivo |
| `F` | Obturación de fisura |
| `1` | Obturación de una superficie |
| `2` | Obturación de 2 o más superficies |
| `3` | Corona por cualquier motivo |
| `4` | Carilla |
| `5` | Cuidado de la pulpa y restauración |
| `6` | Extracción |
| `7` | Necesidad de otra asistencia |

##### 3.6 Erupción Dentaria

> Aplicable de **5 a 18 años**.

Misma estructura de 4 cuadrantes que la sección 3.5, pero con un solo select por diente.

Valores: `0`=No erupcionado, `1`=Un tercio, `2`=Hasta 2/3, `3`=Tres tercios

Nombres de campo: `erupcion_dentaria_NN` (ej: `erupcion_dentaria_17`, `erupcion_dentaria_11`, ..., `erupcion_dentaria_37`)

##### 3.7 Necesidad de Prótesis

> Aplicable a partir de los **15 años**.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Superior | `select` | `protesis_necesidad_superior` | `0`=No necesita, `1`=Necesita y no tiene, `2`=Tiene adecuada, `3`=Tiene inadecuada |
| Inferior | `select` | `protesis_necesidad_inferior` | (igual) |

##### 3.8 Tipo de Prótesis

> Aplicable a partir de los **15 años**.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Superior | `select` | `protesis_tipo_superior` | `0`=No posee, `1`=Parcial removible, `2`=Total, `3`=Fija |
| Inferior | `select` | `protesis_tipo_inferior` | (igual) |

##### 3.9 Hábitos

> Aplicable hasta los **18 años**.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Bruxismo | `select` | `habitos_bruxismo` | `0`=Ausencia, `1`=Presencia |
| Respiración Bucal | `select` | `habitos_respiracion_bucal` | (igual) |
| Deglución Atípica | `select` | `habitos_deglucion_atipica` | (igual) |
| Succión Digital | `select` | `habitos_succion_digital` | (igual) |

> **Nota**: El campo `habitos_ninguno` se calcula automáticamente via trigger SQL. Si todos los hábitos son `0` (ausencia), `habitos_ninguno` = `1`.

##### 3.10 Relación de Oclusión y Maloclusión

| Campo | Tipo HTML | Nombre del Campo | Rango Edad | Valores |
|-------|-----------|-----------------|------------|---------|
| Oclusión 2dos Molares Primarios | `select` | `relacion_oclusion_2dos_molares` | ≤8 años | `0`=Plano terminal recto, `1`=Escalón dental, `2`=Escalón mesial, `3`=PT Recto - E mesial, `4`=PT Recto - E distal, `5`=Escalón distal - Escalón mesial |
| Oclusión 1ros Molares Permanentes | `select` | `relacion_oclusion_1ros_molares` | ≥5 años | `0`=Clase I, `1`=Clase II, `2`=Clase III |
| Apiñamiento | `select` | `apiñamiento` | — | `0`=No hay arcos apiñados, `1`=Un arco apiñado, `2`=Dos arcos apiñados |
| Espaciamiento | `select` | `espaciamiento` | — | `0`=No hay arcos espaciados, `1`=Un arco espaciado, `2`=Dos arcos espaciados |
| Diastema | `select` | `diastema` | — | `0`=Ausencia, `1`=Presencia, `8`=No Aplica |
| Mordida Abierta | `select` | `mordida_abierta` | — | `0`=Ausencia, `1`=Presencia |
| Mordida Profunda Anterior | `select` | `mordida_profunda_anterior` | — | `0`=Ausencia, `1`=Presencia |
| Mordida Tope a Tope | `select` | `mordida_tope_a_tope` | — | `0`=Ausencia, `1`=Presencia |
| Mordida Cruzada | `select` | `mordida_cruzada` | — | `0`=Sin alteración, `1`=Anterior, `2`=Posterior unilateral, `3`=Posterior bilateral, `4`=Anterior y posterior |

#### Sección 4: Necesidad Inmediata de Asistencia y Consulta

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Trastorno que amenaza la vida | `select` | `necesidad_asistencia_trastorno` | `0`=Ausencia, `1`=Presencia |
| Dolor o infección | `select` | `necesidad_asistencia_dolor` | `0`=Ausencia, `1`=Presencia |

---

## 5. Estructura del Formulario — Socioeconómico (`socioeconomico.html`)

### 5.1 Secciones

#### Datos del Paciente
Iguales al formulario de salud bucal: cédula, nombre, apellido, sexo.

#### 1. Información General

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Edad | `input[type=number]` | `edad` | Requerido |
| Autoidentificación Étnica | `select` | `autoidentificacion_etnica` | `1`=Indígena, `2`=Mestizo, `3`=Blanco, `4`=Afrovenezolano, `5`=Otro |
| Etnia Indígena | `select` | `etnia_indigena` | Solo si autoidentificación=1. `0`=No aplica, `1`=Wayúu, `2`=Añu, `3`=Barí, `4`=Yukpa, `5`=Japreria, `6`=Otro pueblo |
| Etnia Otro | `input[text]` | `etnia_otro` | Solo si autoidentificación=5. Input libre |

#### 2. Condición de Salud General

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Discapacidad funcional | `select` | `discapacidad_funcional` | `1`=Sí, `2`=No, `0`=No sabe |
| Nivel de independencia | `select` | `nivel_independencia` | `1`=Totalmente dependiente, `2`=Medianamente dependiente, `3`=Totalmente independiente |

#### 3. Condición Socioeconómica y Familiar

##### 3.1 Situación Familiar

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Tiene familiares directos | `select` | `tiene_familiares` | `1`=Sí, `2`=No, `0`=No sabe / No recuerda |
| Frecuencia de visitas | `select` | `frecuencia_visitas` | `1`=Semanal, `2`=Quincenal, `3`=Mensual, `4`=Ocasional, `5`=Nunca |

##### 3.2 Situación Socioeconómica (Multirespuesta)

> Si "Ninguno"=Sí, los demás campos se deshabilitan.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Pensión | `select` | `ingresos_pension` | `1`=Sí, `2`=No |
| Jubilación | `select` | `ingresos_jubilacion` | `1`=Sí, `2`=No |
| Ayuda de familiares | `select` | `ingresos_familiares` | `1`=Sí, `2`=No |
| Bonos del Estado | `select` | `ingresos_bonos` | `1`=Sí, `2`=No |
| Ninguno | `select` | `ingresos_ninguno` | `1`=Sí, `2`=No (default) |
| Otro | `select` | `ingresos_otro` | `1`=Sí, `2`=No |
| Especifique cuál | `input[text]` | `ingresos_otro_text` | Solo si "Otro"=Sí |
| Necesidades cubiertas | `select` | `necesidades_cubiertas` | `1`=Sí todas, `2`=Parcialmente, `3`=No, `0`=No sabe / No responde |

#### 4. Hábitos de Higiene Bucal

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Frecuencia cepillado | `select` | `frecuencia_cepillado` | `1`=Después de cada comida, `2`=Dos veces al día, `3`=Una vez al día, `4`=Ocasionalmente, `5`=No realiza limpieza |
| Quién hace la higiene | `select` | `quien_higiene` | `1`=Lo hace solo(a), `2`=Con ayuda de cuidador o personal, `3`=Lo hace solo el cuidador |

##### Productos de higiene (Multirespuesta)

> Si "Ninguno"=Sí, los demás se deshabilitan.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Cepillo dental | `select` | `producto_cepillo` | `1`=Sí, `2`=No |
| Crema dental | `select` | `producto_crema` | `1`=Sí, `2`=No |
| Enjuague bucal | `select` | `producto_enjuague` | `1`=Sí, `2`=No |
| Hilo dental | `select` | `producto_hilo` | `1`=Sí, `2`=No |
| Productos para prótesis | `select` | `producto_protesis` | `1`=Sí, `2`=No |
| Ninguno | `select` | `producto_ninguno` | `1`=Sí, `2`=No (default) |

#### 5. Uso y Estado de Prótesis Dental

> Los campos de prótesis se deshabilitan si "Usa prótesis"=No.

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Usa prótesis | `select` | `usa_protesis` | `1`=Sí, `2`=No |
| Desde cuándo | `select` | `desde_cuando_protesis` | `0`=No aplica, `1`=Menos de un mes, `2`=6 meses, `3`=Más de un año, `4`=2 o 3 años, `5`=Más de 5 años |
| Cómo adquirió | `select` | `como_adquirio_protesis` | `0`=No aplica, `1`=Particular, `2`=Misión Sonrisa, `3`=Otra forma |
| Limpia boca | `select` | `limpia_boca_protesis` | `0`=No aplica, `1`=Siempre, `2`=A veces, `3`=Nunca |
| Limpia dentadura | `select` | `limpia_dentadura_protesis` | `0`=No aplica, `1`=Cepillo y pasta normal, `2`=Cepillo y jabón suave, `3`=Productos especiales, `4`=Solo agua, `5`=No las limpia |

##### Problemas con la prótesis

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Dolor | `select` | `protesis_dolor` | `0`=No aplica, `1`=Seguido, `2`=A veces, `3`=No |
| Se le mueven | `select` | `protesis_mueven` | `0`=No aplica, `1`=Seguido, `2`=A veces, `3`=No |
| Se le ha roto | `select` | `protesis_rota` | `0`=No aplica, `1`=Seguido, `2`=A veces, `3`=No |
| Otra molestia | `select` | `protesis_otra_molestia` | `0`=No aplica, `1`=Sí, `2`=No |
| Especifique otra molestia | `input[text]` | `protesis_otra_molestia_text` | Solo si "Otra molestia"=Sí |

#### 6. Malestar Bucal

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Dolor bucal | `select` | `dolor_bucal` | `1`=Seguido, `2`=A veces, `3`=No |
| Encías sangran | `select` | `encias_sangran` | `1`=Sí, `2`=No, `0`=No sabe |

#### 7. Atención Dental

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Última vez odontólogo | `select` | `ultima_vez_odontologo` | `1`=Hace menos de 6 meses, `2`=Hace de 6 meses a 1 año, `3`=Hace más de 1 año, `4`=Nunca lo ha examinado |

#### 8. Hábitos Perjudiciales

| Campo | Tipo HTML | Nombre del Campo | Valores |
|-------|-----------|-----------------|---------|
| Es fumador | `select` | `es_fumador` | `1`=Sí, `2`=No |
| Cantidad cigarrillos | `input[type=number]` | `cantidad_cigarrillos` | Solo visible si fumador=Sí. Min 0 |
| Consumo alcohol | `select` | `consumo_alcohol` | `1`=Sí, `2`=No |
| Frecuencia alcohol | `select` | `frecuencia_alcohol` | `0`=No aplica, `1`=Diaria, `2`=3 veces a la semana, `3`=Semanal, `4`=Ocasional, `5`=Nunca |

---

## 6. Estilos CSS (style.css)

### 6.1 Variables CSS (Custom Properties)

```css
:root {
    --smallpad: clamp(10px, 1.5vw, 20px);   /* Padding pequeño */
    --pad: clamp(16px, 2.5vw, 34px);         /* Padding mediano */
    --bigpad: clamp(28px, 5vw, 64px);        /* Padding grande */
    --bg: #e5f5ff;                           /* Fondo principal (celeste claro) */
    --bg2: #006699;                          /* Fondo de secciones (azul medio) */
    --accent: #660099;                       /* Acento púrpura */
    --fg: #003c6b;                           /* Texto principal (azul oscuro) */
    --card-max: min(95vw, 500px);            /* Ancho máximo de tarjetas */
    font-family: 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell,
                 'Helvetica Neue', Arial, sans-serif;
}
```

### 6.2 Breakpoints Responsivos

| Ancho Mínimo | `--card-max` | Cambios |
|-------------|-------------|---------|
| 0px (default) | `min(95vw, 500px)` | Estilos base |
| 480px | `min(90vw, 560px)` | Card un poco más ancha |
| 768px | `min(90vw, 820px)` | `justify-content: flex-start`, ajustes de padding |
| 1024px | `min(80vw, 920px)` | Card más ancha para desktop |

En pantallas ≥ 768px, las páginas `#welcome`, `#login`, `#config`, `#form` cambian `justify-content` a `flex-start` y ajustan el padding top.

### 6.3 Paleta de Colores

| Variable | Hex | Uso Principal |
|----------|-----|--------------|
| `--bg` | `#e5f5ff` | Fondo de página (celeste claro) |
| `--bg2` | `#006699` | Fondo de contenedores principales, botón primario, headers |
| `--accent` | `#660099` | Borde de botón info, acento púrpura |
| `--fg` | `#003c6b` | Texto principal, encabezados h1/h2 |
| — | `#FFFFFF` | Tarjetas, fondos de inputs, botones secundarios |
| — | `#e0e0e0` | Botones terciarios, links de navegación |
| — | `#555` | Texto secundario, labels |
| — | `#999` | Notas al pie, notas de texto, separadores |
| — | `#d4880f` | Botón de emergencia (borde dashed) |
| — | `#fff8e6` | Fondo del botón de emergencia |

### 6.4 Estilos de Componentes

#### Tarjetas (Cards)
Todas las tarjetas comparten estos estilos base:
- `background-color: white`
- `border-radius: 10px`
- `box-shadow: 0px 4px 10px rgba(0,0,0,0.3)`
- `max-width: var(--card-max)`
- `padding: var(--bigpad)`
- `display: flex; flex-direction: column; align-items: center`

#### Botones

| Clase | Fondo | Texto | Borde | Sombra | Notas |
|-------|-------|-------|-------|--------|-------|
| `.btn-primary` | `var(--bg2)` | Blanco | Ninguno | `0px 3px 5px` | Bold, font-size 1.1rem |
| `.btn-secondary` | Blanco | `var(--bg2)` | 2px solid `var(--bg2)` | `0px 2px 4px` | Bold, font-size 1.05rem |
| `.btn-tertiary` | `#e0e0e0` | `#555` | Ninguno | `0px 2px 4px` | Normal weight |
| `.btn-emergency` | `#fff8e6` | `#d4880f` | 2px dashed `#d4880f` | Ninguno | Bold 600 |
| `.btn-info` | Blanco | `var(--accent)` | 2px solid `var(--accent)` | `0px 2px 4px` | Bold 600 |
| `input[type=submit]` | `var(--bg2)` | Blanco | Ninguno | `0px 3px 5px` | Bold, cursor pointer |

Todos los botones: `padding: var(--pad)`, `border-radius: 5px`, `width: 100%`, `text-align: center`

#### Formulario (#form)

- **Contenedor `#form`**: fondo `--bg2`, flexbox column centrado, padding top compensa el botón "Volver"
- **Tarjeta `#form > form`**: fondo blanco, border-radius 10px, sombra, max-width `--card-max`, overflow hidden
- **`.scroll-area`**: contenedor scrolleable interno con `overflow-y: auto`, `flex: 1`, `min-height: 0`
- **`.options`**: `display: flex; flex-wrap: wrap; width: 100%` — cada hijo es `flex: 1 1 180px`

#### Tablas

- `border-collapse: collapse`
- `.small-select`: selects con dimensiones reducidas (`width: 1.5rem; height: 2rem`)
- `.table-scroll`: `overflow-x: auto; max-width: 100%; -webkit-overflow-scrolling: touch`
- `th { font-weight: normal }` — encabezados sin negrita

#### Separadores

- `hr.divider`: `border: none; border-top: 1px solid #ddd; width: 100%`
- `.separator`: `flex-basis: 2px; background-color: #999; opacity: 0.3; margin: var(--smallpad) 0` — línea entre secciones

#### Modales

- `.modal-overlay`: `position: fixed; top:0; left:0; right:0; bottom:0; background: rgba(0,0,0,0.5); z-index: 100; display: none; justify-content: center; align-items: center`
- `.modal-card`: tarjeta blanca centrada dentro del overlay, con `max-width: var(--card-max)`

### 6.5 Tipografía

| Elemento | Tamaño | Estilo | Notas |
|----------|--------|--------|-------|
| `html` (base) | `clamp(16px, 2.5vw, 28px)` | — | Tamaño base responsivo |
| `h1` (form) | 1.4rem | Bold | border-top/bottom 1px solid `#ddd`, centrado |
| `h2` (form) | 1.2rem | Bold | Sin border |
| `label` | `clamp(1rem, 1.5vw, 1.1rem)` | Normal | Color `#555` |
| `span.note` | 0.9rem | Normal | Color `#999`, centrado |
| Botones primarios | 1.1rem | Bold | — |
| Botones secundarios | 1.05rem | Bold | — |
| Botones terciarios | 0.95rem | Normal | — |
| Copyright | 0.7rem | Normal | Color `#999`, centrado |
| Footer tarjeta | 0.7rem | Normal | Color `#aaa` |

### 6.6 Input y Select

```css
select, input {
    width: 10em;
    background-color: white;
    color: black;
    border-radius: 5px;
}
```

Dentro de `.scroll-area`: `width: 100%; box-sizing: border-box; padding: var(--smallpad); font-size: 1rem`

---

## 7. Lógica de Negocio (JavaScript)

### 7.1 Comportamiento por Rango de Edad

| Sección | Edad Mín | Edad Máx | Comportamiento |
|---------|----------|----------|---------------|
| Lesión (`#lesion`) | 5 | — | Disabled si edad < 5 |
| Temporomandibular (`#temporomaxilar`) | 12 | — | Disabled si edad < 12 |
| Periodontico (`table[id="periodontico"]`) | 15 | — | Opciones 3 y 4 ocultas si edad < 15 |
| Fijación (`table[id="fijacion"]`) | 15 | — | Disabled si edad < 15 |
| Erupción Dentaria (`#erupcion`) | 5 | 18 | Disabled si edad < 5 o > 18 |
| Prótesis (`[name^="protesis_"]`) | 15 | — | Disabled si edad < 15 |
| Oclusión 1ros Molares | 5 | — | Disabled si edad < 5 |
| Oclusión 2dos Molares | — | 8 | Disabled si edad > 8 |
| Fluorosis | 6 | — | Disabled si edad < 6 |
| Hábitos (`#habitos`) | — | 18 | Disabled si edad > 18 |

### 7.2 Lógica de Lesión (Deshabilitado Dinámico)

Cuando "Hay Lesión" = `1` (No), todos los campos `[name^="lesion_"]` se deshabilitan y se les agrega una opción `8 - No Aplica` con valor `8`. Cuando cambia a `0` (Si), se restauran.

### 7.3 Lógica de Municipio/Parroquia (Por Estado)

- Si `estado` = "Zulia": se muestra el `select` de municipios/parroquias y se oculta el `input` libre.
- Si `estado` ≠ "Zulia": se muestra el `input` libre y se oculta el `select`.

### 7.4 Lógica de Etnia del Zulia

- El select `etnia_zulia` solo se habilita si `etnia` = `3` (Indígena) Y `estado` = "Zulia".
- En otros casos, se deshabilita.

### 7.5 Lógica Multirespuesta (Socioeconómico)

- **Ingresos**: Si `ingresos_ninguno` = `1` (Sí), todos los demás campos de ingresos se deshabilitan y se fuerzan a `2` (No).
- **Productos**: Si `producto_ninguno` = `1` (Sí), todos los demás campos de productos se deshabilitan y se fuerzan a `2` (No).

### 7.6 Cifrado de IP del Servidor

```java
SECRET_KEY = 0xA23A23D;

// Cifrado (para generar el código):
encrypted = (IP_as_int) ^ SECRET_KEY;
code = Long.toString(encrypted, 36);  // Base 36

// Descifrado (en el cliente):
ipInt = Long.parseLong(code, 36) ^ SECRET_KEY;
ip = ((ipInt >> 24) & 0xFF) + "." + ((ipInt >> 16) & 0xFF) + "." +
     ((ipInt >> 8) & 0xFF) + "." + (ipInt & 0xFF);
```

### 7.7 Autenticación

1. Usuario ingresa cédula y contraseña en `login.html`
2. `post.js` envía JSON al Java: `Android.post("login", json)`
3. Java hashea la contraseña con **SHA-1**
4. Consulta `SELECT nombre FROM usuarios WHERE cedula = ? AND password = ?`
5. Si coincide, guarda nombre/cedula/hash en variables de sesión
6. Redirige a `welcome.html`

### 7.8 Sincronización de Usuarios

Al configurar la IP del servidor (o al iniciar la app):
1. Se llama a `GET http://<ip>:8080/syncUsers`
2. Descarga `{ "dataUsers": [ { "cedula", "nombre", "password" }, ... ] }`
3. Reemplaza la tabla `usuarios` local
4. Permite login offline con credenciales sincronizadas

---

## 8. Archivos del Cliente

```
webview/
├── assets/
│   ├── form.html              ← Generado por gen.py (NO editar directamente)
│   ├── socioeconomico.html    ← Formulario socioeconómico (HTML estático)
│   ├── login.html             ← Pantalla de login
│   ├── config.html            ← Pantalla de configuración (código IP)
│   ├── welcome.html           ← Menú principal
│   ├── info.html              ← Información de créditos
│   ├── style.css              ← Hoja de estilos compartida
│   ├── post.js                ← Intercepta submits HTML → Android.post()
│   ├── escudo_de_luz.png      ← Logo LUZ
│   └── LOGO ODONTOLOGIA.png   ← Logo Facultad de Odontología
├── form.jinja.html            ← Template Jinja2 (fuente de form.html)
├── gen.py                     ← Generador: form.jinja.html → form.html
├── schema.sql                 ← Schema SQLite/MySQL
├── Main.java                  ← Entrypoint Android + Handler JS bridge
├── AndroidManifest.xml        ← Manifiesto de Android
├── android-build              ← Script de compilación del APK
└── layout/main.xml            ← Layout Android (WebView full-screen)
```

> **Importante**: `form.html` es generado por `python3 webview/gen.py` a partir de `form.jinja.html`. Nunca editar `form.html` directamente; siempre modificar `form.jinja.html` y regenerar.
