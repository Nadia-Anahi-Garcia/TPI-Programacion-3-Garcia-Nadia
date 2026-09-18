// Importamos la interfaz
import type { IUser } from "../../../types/IUser";
import "../../../style.css";

// Obtenemos los elementos del DOM.
const form = document.querySelector<HTMLFormElement>("#form");

if (form){
    form.addEventListener("submit", (event : SubmitEvent)=>{
        event.preventDefault();  // evita que el navegador recargue la página automáticamente.

        const formData = new FormData(form);
        const email = formData.get("email");
        const password = formData.get("password");

        if (typeof email !== "string" || typeof password !== "string"){
            return;
        }
           
        const newUser: IUser = {
            email: email,
            password: password,
            loggedIn: false,
            role: "client",
        };

        const savedUsers = localStorage.getItem("users");
        const users : IUser[] = savedUsers ? JSON.parse(savedUsers) as IUser[] : [];
        
        const emailExists = users.some((user) => user.email === email);

        // Válida que si el email existe salga un alerta
        if (emailExists) {
            alert("Ya existe un usuario registrado con ese email.");
            return;
        }

        // Agrega un nuevo objeto al array
        users.push(newUser);

        localStorage.setItem("users", JSON.stringify(users));
        alert("Usuario registrado correctamente.");
        // Limpia los campos del registro
        form.reset();


    })
}