// Importamos la interfaz
import type { IUsuario } from "../../../types/usuario";
import { obtenerUsuarios } from "../../../utils/fetch";
import { saveUser } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { obtenerPedidosLocales } from "../../../utils/pedidos";

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



        const usuarios = await obtenerUsuarios();

        const idsClientesConPedidos = obtenerPedidosLocales().map(
            (pedido) => pedido.usuarioDto?.id ?? pedido.idUsuario ?? 0
        );
        const emailExists = usuarios.some((usuario) => usuario.mail === email);
        
        // Válida que si el email existe salga un alerta
        if (emailExists) {
            alert("Ya existe un usuario registrado con ese email.");
            return;
        }

        const newUser: IUsuario = {
            id: Math.max(
                0,
                ...usuarios.map((usuario) => usuario.id),
                ...idsClientesConPedidos
            ) + 1,
            nombre: nombre,
            apellido: apellido,
            celular: celular,
            mail: email,
            rol: "USUARIO",
            eliminado: false
        };


        saveUser(newUser)
        alert("Usuario registrado correctamente.");

        // Limpia los campos del registro
        form.reset();

        // Lo lleva directamente al catálogo.
        navigate("/src/pages/store/home/home.html");




    })
}