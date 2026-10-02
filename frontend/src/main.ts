import { checkAuhtUser } from "./utils/auth";
import { getUSer } from "./utils/localStorage";
import { navigate } from "./utils/navigate";
import "./style.css";


const protegerRutas = (): void => {
  const rutaActual = window.location.pathname;

  if (rutaActual.includes("/admin/")) {
    checkAuhtUser(
      "/src/pages/auth/login/login.html",
      "/src/pages/store/home/home.html",
      "ADMIN"
    );
  } else if (rutaActual.includes("/client/")) {
    checkAuhtUser(
      "/src/pages/auth/login/login.html",
      "/src/pages/admin/home/home.html",
      "USUARIO"
    );
  } else if (rutaActual.includes("/store/")) {
    const usuarioGuardado = getUSer();

    if (!usuarioGuardado) {
      navigate("/src/pages/auth/login/login.html");
    }
  }
};
protegerRutas();