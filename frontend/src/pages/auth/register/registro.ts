// Importamos la interfaz
import type { IUsuario } from "../../../types/usuario";
import { obtenerUsuarios } from "../../../utils/fetch";

// Obtenemos los elementos del DOM.
const form = document.querySelector<HTMLFormElement>("#form");

if (form){
    form.addEventListener("submit", async(event : SubmitEvent)=>{
        event.preventDefault();  // evita que el navegador recargue la página automáticamente.


        const formData = new FormData(form);
        const email = formData.get("email");
        const password = formData.get("password");
        const nombre = formData.get("nombre");
        const apellido = formData.get("apellido");
        const celular = formData.get("celular");

        if (
            typeof nombre !== "string" ||
            typeof apellido !== "string" ||
            typeof celular !== "string" ||
            typeof email !== "string" ||
            typeof password !== "string"
        ) {
            return;
        }
           

        
        const usuariosBase  = await obtenerUsuarios();

        const savedUsers = localStorage.getItem("users");
        const usuariosRegistrados: IUsuario[] = savedUsers
             ? JSON.parse(savedUsers) as IUsuario[]
            : [];

        const usuarios = [...usuariosBase, ...usuariosRegistrados];

        const emailExists = usuarios.some((usuario) => usuario.mail === email);
        
        // Válida que si el email existe salga un alerta
        if (emailExists) {
            alert("Ya existe un usuario registrado con ese email.");
            return;
        }

        const newUser: IUsuario = {
            id: Math.max(...usuarios.map((usuario) => usuario.id), 0) + 1,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            mail: email,
            password: password,
            rol: "USUARIO",
            eliminado: false
        };

        // Agrega un nuevo objeto al array
        usuariosRegistrados.push(newUser);

        localStorage.setItem("users", JSON.stringify(usuariosRegistrados));
        alert("Usuario registrado correctamente.");
        // Limpia los campos del registro
        form.reset();


    })
}