# v5.11.0
## Agregado
- Se agrega dialogo que se muestra en pantalla completa y con nuevo estilo

# v5.10.1
## Arreglado
- Se corrige el comportamiento del teclado cuando el componente `TextField` toma el foco.
- Se corrige la reconstrución de la instancia del componente `TextField` cuando ocurre un cambio de configuración.

# v5.10.0
## Nuevo
- Se agrega el color ui_components_action_bar_text_color a la paleta.

# v5.9.0
## Nuevo
- Se marca como deprecado el Theme.MLTheme, en pos del nuevo Theme.Base que favorece la agregacion haciendo que cada negocio lo implemente con sus propios items

# v5.8.0
## Cambiado
- Actualizado bintray user

# v5.7.2
## Agregado
- Se agrega metodo a TypefaceHelper para obtener un typeface. `#getTypeface(context, font, fontCallback)`. El mismo se recibe desde el callback ya que dependiendo de la imple puede ser sincronico o asincronico el fetch

# v5.7.0
## Nuevo
- Se agrega un theme `Theme.Base` que tiene **temporalmente** como parent a `Theme.MLTheme`. Este theme se encuentra vacio para ser overwriteado por los configurators de apps correspondientes.
- Se agrega una interfaz `TypefaceHelper.TypefaceSetter` que permite customizar el comportamiento de como el `TypefaceHelper` setea las fuentes, esto nos permite descoplarnos de `Calligraphy` para el seteo de las mismas y utilizar otros features availables como Downloadable Fonts o alguna imple propia.

# v5.6.2
## Arreglado
- Para android 23+ se muestra el status bar light (con icono oscuros).

# v5.6.1
## Arreglado
- No publicó 5.6.0

# v5.6.0 
### Arreglado
- Fix memory leak en el MeliSpinner

# v5.5.1
### Arreglado
- No incluye nada, es una publicación para limpiar el delta de commits de develop para que no parezca que hay que relseasearla.

# v5.5.0
### Nuevo
- Se agregaron otros 3 colores para poder customizar el theme desde las apps.

# v5.4.1
- MLTheme customizations added

# v5.4.0
- MLTheme ahora permite ser customizado

# v5.3.0
- MLTheme deja de extender de MLThemeLegacy y ahora permite ser customizado

# v5.2.1
- Fix font set on MeliButton
- Dimens added

# v5.2.0
- Changes

# v5.1.0
- Turn on CircleCI 
