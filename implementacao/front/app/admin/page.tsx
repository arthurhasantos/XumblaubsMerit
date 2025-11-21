"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";

const AdminPage = () => {
  const { user, logout } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  return (
    <ProtectedRoute requireAdmin={true}>
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
            Painel Administrativo
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
            Bem-vindo, {user?.name}!
          </h2>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            Esta é a área administrativa do sistema. Aqui você pode gerenciar todos os dados do sistema.
          </p>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-blue-50 dark:bg-blue-900/20 p-6 rounded-lg border border-blue-200 dark:border-blue-800 flex flex-col">
              <div className="flex items-center mb-4">
                <div className="bg-blue-100 dark:bg-blue-800 p-3 rounded-lg">
                  <svg className="w-6 h-6 text-blue-600 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z" />
                  </svg>
                </div>
                <h3 className="text-lg font-medium text-blue-900 dark:text-blue-100 ml-3">
                  CRUD Alunos
                </h3>
              </div>
              <p className="text-blue-700 dark:text-blue-300 text-sm mb-4 flex-grow">
                Gerencie os dados dos alunos do sistema, incluindo informações pessoais, cursos e saldo de moedas.
              </p>
              <button
                onClick={() => router.push('/admin/alunos')}
                className="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
              >
                Acessar CRUD
              </button>
            </div>
            
            <div className="bg-green-50 dark:bg-green-900/20 p-6 rounded-lg border border-green-200 dark:border-green-800 flex flex-col">
              <div className="flex items-center mb-4">
                <div className="bg-green-100 dark:bg-green-800 p-3 rounded-lg">
                  <svg className="w-6 h-6 text-green-600 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                  </svg>
                </div>
                <h3 className="text-lg font-medium text-green-900 dark:text-green-100 ml-3">
                  CRUD Empresas
                </h3>
              </div>
              <p className="text-green-700 dark:text-green-300 text-sm mb-4 flex-grow">
                Gerencie as empresas parceiras que oferecem vantagens e benefícios para os alunos.
              </p>
              <button
                onClick={() => router.push('/admin/empresas')}
                className="w-full bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
              >
                Acessar CRUD
              </button>
            </div>
            
          </div>
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default AdminPage;
