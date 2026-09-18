import { checkAuhtUser } from "./utils/auth";
import "./style.css";
const protegerRutas = () : void =>{
    const rutaActual = window.location.pathname

    if(rutaActual.includes("/admin/")){
        checkAuhtUser(
            "/src/pages/auth/login/login.html",
            "/src/pages/client/home/home.html",
            "admin"
        );
    } else if (rutaActual.includes("/client/")){
        checkAuhtUser(
            "/src/pages/auth/login/login.html",
            "/src/pages/admin/home/home.html",
            "client"
        );
    }
}

protegerRutas();