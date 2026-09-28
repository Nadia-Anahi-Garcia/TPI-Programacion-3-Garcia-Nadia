import type { Rol } from "./Rol";

export interface IUsuario {
  id: number;
  nombre: string;
  apellido: string;
  mail: string;
  celular: string;
  password: string;
  rol: Rol;
  eliminado: boolean;
}
