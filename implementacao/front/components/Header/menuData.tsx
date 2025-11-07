import { Menu } from "@/types/menu";

const menuData: Menu[] = [
  {
    id: 5,
    title: "Painel Administrativo",
    path: "/admin",
    newTab: false,
    requireAdmin: true,
  },
  {
    id: 6,
    title: "Gerenciar Vantagens",
    path: "/empresa/vantagens",
    newTab: false,
    requireEmpresa: true,
  },
];
export default menuData;
