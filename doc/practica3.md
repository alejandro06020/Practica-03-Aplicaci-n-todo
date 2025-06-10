# 📘 Documentación de practica 3: Aplicacion ToDo

## 📌 Introducción

Esta aplicación parte de la aplicación base proporcionada por el profesor por lo que en esta documentación solo se abordara la explicación de las nuevas características desarrolladas para esta práctica.

> Todas las funcionalidades base de la aplicación se han mantenido igual.

Las nuevas funcionalidades agregadas a la aplicación que se explicaran en este documento son:
- Barra de menú
- Página de listado de usuarios.
- Página de descripción de usuario.

---
## 📚 Barra de Menu

La barra de menú se agrego a diferentes plantillas ya existentes, la barra de menú es un elemento que se encuentra en la parte superior de las plantillas y contine los siguientes elementos:
-	ToDoList: Si se presiona se redirige a la página donde se presenta la una descripción de la aplicación.
-	Tareas: Este botón solo se presenta cuando hay un usuario logeado, si se presiona se redirige a la pagina de lista de tareas del usuario.
-	Cuenta: Este elemento solo se presenta cuando el usuario este logeado y es un elemento dorpdown que tiene el nombre del usuario y al presionarse muestra las opciones de cuenta y de cerrar la sección.
-	Login: Este botón solo se muestra cuando no hay un usuario logueado y te redirige a la página para iniciar cesión.
-	Registrar: Este botón solo se muestra cuando no hay un usuario logueado y te redirige a la página para crear una cuenta.

Para el funcionamiento correcto de la barra de menú se creó el código:

```html
<nav class="navbar navbar-expand-lg navbar-light bg-info">
		  <a class="navbar-brand" href="#">Menu</a>
		  <div class="collapse navbar-collapse" id="navbarNavDropdown">
		    <ul class="navbar-nav" style="width:100%">
		      <li class="nav-item active">
		        <a class="nav-link" th:href="@{/usuarios/{id}/aboutIn(id=${usuario.id})}">ToDoList<span class="sr-only">(current)</span></a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" th:href="@{/usuarios/{id}/tareas(id=${usuario.id})}">Tareas</a>
		      </li>
		      <li class="nav-item dropdown" style="margin-left:70%">
		        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" 
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" th:text="${usuario.nombre}">
		        </a>
		        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
		          <a class="dropdown-item" href="#">Cuenta</a>
		          <a class="dropdown-item" href="/logout"th:text="'Cerrar sesion ' + ${usuario.nombre}">Cerrar secion</a>
		        </div>
		      </li>
		    </ul>
		  </div>
		</nav>
```

El cual se agrego a las siguientes plantillas:
-	about.html
-	formEditarTarea.html
-	formNuevaTarea.html
-	listaTareas.html

Adicionalmente se creo una nueva plantilla *aboutIn.html* la cual se muestra la barra de menú cuando hay un usuario logueado, la nueva plantilla es igual a la *about* normal solo que, la barra de menú de esta ultima es la que se muestra si el usuario no esta logueado y es:
```html
<nav class="navbar navbar-expand-lg navbar-light bg-info">
		<a class="navbar-brand" href="#">Menu</a>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav" style="width:100%">
				<li class="nav-item active">
					<a class="nav-link" href="/about">ToDoList<span class="sr-only">(current)<</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="/login">Login</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="/registro">Registrarse</a>
				</li>
			</ul>
		</div>
	</nav>
```

Para el correcto funcionamiento de todos los elementos de la barra de manu se creó un nuevo método controlador*HomeController* el cual hace las redirecciones a la nueva plantilla, todas las demás redirecciones usan métodos ya establecidos en la app base, el nuevo método es:
```java
@Controller
public class HomeController {
	@Autowired
	UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }
    //Esta pagina about se mostrara a los usuarios no logeados
    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
    //Creamos una nueva pagina about para usuarios logeados
    @GetMapping("/usuarios/{id}/aboutIn")
    public String aboutIn(@PathVariable(value="id") Long idUsuario,Model model,HttpSession session) {
    	comprobarUsuarioLogeado(idUsuario);
    	UsuarioData usuario = usuarioService.findById(idUsuario);
    	model.addAttribute("usuario", usuario);
    	return "aboutIn";
    }
}
```

Para probar el funcionamiento del método se creo un nuevo test en *AcercaDeWebTest* el cual esta hecho de la siguiente manera:
```java
    //Test para probar la pagina about que se muestra a los usuarios logueados
    @Test
    public void getAboutInDevuelveNombreAplicacion() throws Exception {
    	// Un usuario con dos tareas en la BD
        Long usuarioId = addUsuarioTareasBD().get("usuarioId");
        
        // Moqueamos el método usuarioLogeado para que devuelva el usuario 1L,
        // el mismo que se está usando en la petición. De esta forma evitamos
        // que salte la excepción de que el usuario que está haciendo la
        // petición no está logeado.
        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);
        //Hacemos la peticion get a la nueva pagina todo
        String url = "/usuarios/" + usuarioId.toString() + "/aboutIn";
        this.mockMvc.perform(get(url))
                .andExpect(content().string(containsString("Menu")));
    }
```
Esas son todos los cambios relacionados con la barra de menú.


---

## 📋 Página de lista de usuarios

Se creo una nueva plantilla *listaUsuarios.html* la cual nos lista los usuarios, su estructura es la siguiente:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:replace="fragments :: head (titulo='Lista de usaurios')"></head>

<body>
	<div class="container-fluid">
	    <div class="row mt-3">
	        <div class="col">
	            <h2>Lista de usuarios</h2>
	        </div>
	    </div>
	    <div class="row mt-3">
	        <div class="col">
	            <table class="table table-striped">
	                <thead>
	                <tr>
	                    <th>Id</th>
	                    <th>Correo Electronico</th>
	                </tr>
	                </thead>
	                <tbody>
	                <tr th:each="usuario: ${usuarios}">
	                    <td th:text="${usuario.id}"></td>
	                    <td th:text="${usuario.email}"></td>
	                    <td><a class="btn btn-primary btn-xs" th:href="@{/registrados/{id}(id=${usuario.id})}"/>Descripcion</a>
	                    </td>
	                </tr>
	                </tbody>
	            </table>
	        </div>
	    </div>
	    <div class="row mt-2">
	        <div class="col">
	            <div class="alert alert-success alert-dismissible fade show" role="alert" th:if="${!#strings.isEmpty(mensaje)}">
	                <span th:text="${mensaje}"></span>
	                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	        </div>
	    </div>
	</div>
<div th:replace="fragments::javascript"/>

</body>
</html>
```
Para el acceso a esta nueva plantilla se creó un nuevo controlador *ListarUsuariosController* el cual se encarga de las redirecciones(/registrados) a la nueva plantilla. El código del nuevo controlador es:
```java
@Controller
public class ListarUsuariosController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/registrados")
	public String listadoUsuarios(Model model, HttpSession session) {
		List<UsuarioData> usuarios = usuarioService.getAll();
		model.addAttribute("usuarios", usuarios);
		return "listaUsuarios";
	}
}
```
Para asegurar el funcionamiento de la nueva plantilla se creó un nuevo método en la clase *UsuarioService* para listar a todos los usuarios:
```java
    //Metodo para enviar la lista de usuarios
    @Transactional(readOnly = true)
    public List<UsuarioData> getAll(){
    	logger.debug("Devolviendo todas los usuarios" );
    	List<Usuario> p = (List<Usuario>) usuarioRepository.findAll();
    	if(p==null) {
    		throw new TareaServiceException("No hay usuarios");
    	}
    	List<UsuarioData> usuarios =p.stream().map(usuario -> modelMapper.map(usuario,UsuarioData.class))
    			.collect(Collectors.toList());
    	return usuarios;
    }
```
Para asegurarnos de que nuestras nuevas funcionalidades trabajan correctamente se crearon varios nuevos test. Se creo una nueva clase test *ListaUsuariosTest* donde se agregó un test para la redirección a la nueva plantilla:
```java
    //Test para probar el controlador para la pagina de registrados
    @Test
    public void listaUsuarioController() throws Exception {
    	// GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();
        
        // WHEN, THEN
        // se realiza la petición GET al listado de usuarios,
        // el HTML devuelto contiene la lista de usuarios.
        String url = "/registrados";
        this.mockMvc.perform(get(url))
                .andExpect(content().string(containsString("user@ua")));
    }
```
Se agrego un nuevo test sobre la clase *UsuarioServiceTest* para probar el nuevo método para listar usuarios:
```java
    //Test para la lista de usuarios
    @Test
    public void servicioListaUsuarios() {
    	// GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();
        
        //WHEN
        //Se recupera la lista de usuarios
        List<UsuarioData> listaUsuarios = usuarioService.getAll();
        
        //Then
        //Se debe tener una lista con un usuario
        assertEquals(1, listaUsuarios.size());
    }
```
Con estos test se comprobo que las nuevas caracteristicas funcionen sin problema.

Esos son todos los ambios relazionados con esta nueva caracteristica.

---

## 🧑‍💼 Página de descripción de cuenta de usuario
Para la página de descripción de los datos de la cuenta de un usuario se usa el botón creado previamente en la pagina de lista de usuarios, este botón nos redirecciona a una nueva plantilla creada *descripcionUsuario.html* que mostrara todos los datos de la cuenta del usuario seleccionado, su estructura es la siguiente:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments :: head (titulo='Descripcion de usuario')"></head>

<body>
	<div class="row bg-info text-white mt-3">
		<div class="col">
			<h2 th:text="'Descripcion de la cuenta '+ ${usuario.nombre}"></h2>
		</div>
	</div>
	<div class="row mt-3">
		<div class="col">
			<h3 th:text="'Nombre de usuario: '+ ${usuario.nombre}"></h3>
		</div>
	</div>
	<div class="row mt-3">
		<div class="col">
			<h3 th:text="'Id del usuario: '+ ${usuario.id}"></h3>
		</div>
	</div>
	<div class="row mt-3">
		<div class="col">
			<h3 th:text="'Email del usuario: '+ ${usuario.email}"></h3>
		</div>
	</div>
	<div class="row mt-3">
		<div class="col">
			<h3 th:text="'Fecha de nacimiento del usuario: '+ ${usuario.fechaNacimiento}"></h3>
		</div>
	</div>
	
	<div th:replace="fragments::javascript"/>	
</body>
</html>
```
Para manejar esta nueva redirección se agregó un nuevo método sobre el controlador *ListarUsuariosController* el cual se encarga de redirigir a la nueva plantilla:
```java
	@GetMapping("/registrados/{id}")
	public String descripcionUsuario(@PathVariable(value="id") Long idUsuario,Model model, HttpSession session) {
		UsuarioData usuario = usuarioService.findById(idUsuario);
		model.addAttribute("usuario",usuario);
		return "descripcionUsuario";
	}
```
Para comprobar que nuestra nueva característica se agrego un nuevo test sobre la clase *ListaUsuariosTest* el cual comprueba que se este desplegando la información sobre nuestra nueva página:
```java
    //Test para probar la pagina de descripcion de usuario
    @Test
    public void descripcionUsuarioGet() throws Exception {
    	// GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();
        // WHEN, THEN
        // se realiza la petición GET a la descripcion del usuario,
        // el HTML devuelto contiene los datos del usuario.
        String url = "/registrados/"+usuarioId.toString();
        this.mockMvc.perform(get(url)).andExpect(content().string(
        		allOf(containsString("Usuario Ejemplo"),containsString("user@ua"))));
    }
```
Estos son todas las nuevas modificaciones realizadas sobre la aplicación base para agregar estas tres nuevas caracteristicas.

