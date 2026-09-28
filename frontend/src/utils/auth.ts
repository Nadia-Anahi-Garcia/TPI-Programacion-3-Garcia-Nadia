import type { IUsuario } from "../types/usuario";
import type { Rol } from "../types/Rol";
import { getUSer, removeUser } from "./localStorage";
import { navigate } from "./navigate";

export const checkAuhtUser = (
  redireccion1: string,
  redireccion2: string,
  rol: Rol
) => {
  
  const user = getUSer();
  if (!user) {
    navigate(redireccion1);
    return;
  } else {
    const parseUser: IUsuario = JSON.parse(user);
    if (parseUser.rol !== rol) {
      navigate(redireccion2);
      return;
    }
  }
};

export const logout = () => {
  removeUser();
  navigate("/src/pages/auth/login/login.html");
};
