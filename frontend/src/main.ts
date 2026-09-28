import { checkAuhtUser } from "./utils/auth";
import "./style.css";
const protegerRutas = () : void =>{
    const rutaActual = window.location.pathname

    if(rutaActual.includes("/admin/")){
        checkAuhtUser(
            "/src/pages/auth/login/login.html",
            "/src/pages/store/home/home.html",
            "ADMIN"
        );
    } else if (rutaActual.includes("/store/")){
        checkAuhtUser(
            "/src/pages/auth/login/login.html",
           "/src/pages/admin/home/home.html",
            "USUARIO"
        );
    }
}

protegerRutas();