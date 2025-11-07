export type Menu = {
  id: number;
  title: string;
  path?: string;
  newTab: boolean;
  submenu?: Menu[];
  requireAdmin?: boolean;
  requireEmpresa?: boolean;
  requireAluno?: boolean;
};
