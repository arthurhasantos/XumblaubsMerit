import { Menu } from "@/types/menu";

const menuData: Menu[] = [
  {
    id: 1,
    title: "Dashboard",
    path: "/dashboard",
    newTab: false,
    requireAdmin: false,
  },
  {
    id: 2,
    title: "Minhas Moedas",
    path: "/moedas",
    newTab: false,
    requireAdmin: false,
  },
  {
    id: 3,
    title: "Vantagens",
    path: "/vantagens",
    newTab: false,
    requireAdmin: false,
  },
  {
    id: 4,
    title: "Histórico",
    path: "/historico",
    newTab: false,
    requireAdmin: false,
  },
];
export default menuData;
