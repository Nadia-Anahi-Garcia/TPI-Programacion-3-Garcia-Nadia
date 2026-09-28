import type { IUsuario } from "../../../types/usuario";
import { navigate } from "../../../utils/navigate";
import { obtenerUsuarios } from "../../../utils/fetch";
import { saveUser } from "../../../utils/localStorage";

const form = document.querySelector<HTMLFormElement>("#form");


if (form){
  form.addEventListener("submit", async (event : SubmitEvent)=>{
    event.preventDefault();  // evita que el navegador recargue la página automáticamente.

    const formData = new FormData(form);
    const email = formData.get("email");
    const password = formData.get("password");

    if (typeof email !== "string" || typeof password !== "string"){
      return;
    }

    const usuariosBase = await obtenerUsuarios();
    const saveUsers = localStorage.getItem("users");
    const usuariosRegistrados : IUsuario[] = saveUsers
      ? JSON.parse(saveUsers) as IUsuario[] : [];
    
    const usuarios = [...usuariosBase, ...usuariosRegistrados];

    const userFound = usuarios.find(
      (usuario)=>
        usuario.mail === email && 
        usuario.password === password &&
        !usuario.eliminado
    );
    
    if (!userFound){
     alert("Email o password incorrectas")
     return
    }
    
    saveUser(userFound);

    form.reset();

    if (userFound.rol === "ADMIN") {
        navigate("/src/pages/admin/home/home.html")
    }else{
      navigate("/src/pages/client/home/home.html")
    }
});
  

}