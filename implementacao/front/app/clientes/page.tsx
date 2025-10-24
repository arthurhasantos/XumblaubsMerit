"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

const ClientesPage = () => {
  const { user, logout } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!user) {
      router.push('/signin');
    }
  }, [user, router]);

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  if (!user) {
    return <div>Carregando...</div>;
  }

  return (
    <div className="container mx-auto px-4 py-8 pt-24">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
          CRUD de Clientes
        </h1>
        <button
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
        >
          Sair
        </button>
      </div>
      
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
        <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">
          Bem-vindo, {user.email}!
        </h2>
        <p className="text-gray-600 dark:text-gray-400">
          Esta é a área administrativa do sistema. Aqui você pode gerenciar os CRUDs de Alunos e Empresas Parceiras.
        </p>
        
        <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="bg-blue-50 dark:bg-blue-900/20 p-4 rounded-lg">
            <h3 className="text-lg font-medium text-blue-900 dark:text-blue-100 mb-2">
              CRUD Alunos
            </h3>
            <p className="text-blue-700 dark:text-blue-300 text-sm">
              Gerencie os dados dos alunos do sistema
            </p>
          </div>
          
          <div className="bg-green-50 dark:bg-green-900/20 p-4 rounded-lg">
            <h3 className="text-lg font-medium text-green-900 dark:text-green-100 mb-2">
              CRUD Empresas
            </h3>
            <p className="text-green-700 dark:text-green-300 text-sm">
              Gerencie as empresas parceiras
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClientesPage;
