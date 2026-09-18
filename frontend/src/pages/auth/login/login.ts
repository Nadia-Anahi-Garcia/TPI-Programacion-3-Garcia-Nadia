import type { IUser } from "../../../types/IUser";
import { navigate } from "../../../utils/navigate";
import "../../../style.css";
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

    const savedUsers = localStorage.getItem("users");
    const users : IUser[] = savedUsers ? JSON.parse(savedUsers) as IUser[] : [];
    const userFound = users.find((user) => user.email === email && user.password === password);
    
    if (!userFound){
     alert("Email o password incorrectas")
     return
    }
    
    const userData : IUser = {
      email:userFound.email,
      password: userFound.password,
      loggedIn: true,
      role: userFound.role,
    };

    localStorage.setItem("userData", JSON.stringify(userData));
    form.reset();

    if (userData.role === "admin") {
        navigate("/src/pages/admin/home/home.html")
    }else{
      navigate("/src/pages/client/home/home.html")
    }
});
  

}