// Importamos la interfaz
import type { IUsuario } from "../../../types/usuario";
import { obtenerUsuarios } from "../../../utils/fetch";
import { saveUser } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";

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
            id: Math.max(
                0,
                ...usuarios
                    .map((usuario) => usuario.id)
                    .filter((id) => Number.isSafeInteger(id) && id > 0)
            ) + 1,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            mail: email,
            rol: "USUARIO",
            eliminado: false
        };

        // Agrega un nuevo objeto al array
        usuariosRegistrados.push(newUser);

        localStorage.setItem("users", JSON.stringify(usuariosRegistrados));

        saveUser(newUser)
        alert("Usuario registrado correctamente.");

        // Limpia los campos del registro
        form.reset();

        // Lo lleva directamente al catálogo.
        navigate("/src/pages/store/home/home.html");




    })
}