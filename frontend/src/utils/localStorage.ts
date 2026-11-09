import type { IUsuario } from "../types/usuario";

export const saveUser = (user: IUsuario) => {
  const datosSesion = {
    id: user.id,
    nombre: user.nombre,
    apellido: user.apellido,
    mail: user.mail,
    celular: user.celular,
    rol: user.rol,
    eliminado: user.eliminado,
  };

  localStorage.setItem("userData", JSON.stringify(datosSesion));
};
export const getUSer = () => {
  return localStorage.getItem("userData");
};
export const removeUser = () => {
  localStorage.removeItem("userData");
};
