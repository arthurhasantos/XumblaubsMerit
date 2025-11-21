"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";
import { api } from "@/lib/api";
import Link from "next/link";

interface Transacao {
  id: number;
  dataTransacao: string;
  quantidade: number;
  tipo: string;
  motivo: string | null;
  professorId: number;
  professorNome: string;
  alunoId: number;
  alunoNome: string;
}

const HistoricoMoedasPage = () => {
  const { user, token } = useAuth();
  const router = useRouter();
  const [historico, setHistorico] = useState<Transacao[]>([]);
  const [loading, setLoading] = useState(true);
  const [saldoAluno, setSaldoAluno] = useState(0);

  useEffect(() => {
    if (user && user.roles.includes('ALUNO')) {
      fetchHistorico();
      fetchSaldoAluno();
    } else if (user && !user.roles.includes('ALUNO')) {
      toast.error('Acesso negado. Apenas alunos podem acessar esta página.');
      router.push('/');
    }
  }, [user, token, router]);

  const fetchHistorico = async () => {
    try {
      const data = await api.get('/transacoes/aluno');
      setHistorico(data);
    } catch (error) {
      console.error('Erro ao buscar histórico:', error);
      toast.error('Erro ao carregar histórico de moedas');
    } finally {
      setLoading(false);
    }
  };

  const fetchSaldoAluno = async () => {
    try {
      const data = await api.get('/alunos/me');
      setSaldoAluno(data.saldoMoedas || 0);
    } catch (error) {
      console.error('Erro ao buscar saldo:', error);
    }
  };

  if (loading) {
    return (
      <ProtectedRoute>
        <div className="container mx-auto px-4 py-8 pt-24">
          <div className="flex items-center justify-center min-h-screen">
            <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
          </div>
        </div>
      </ProtectedRoute>
    );
  }

  return (
    <ProtectedRoute>
      <div className="container mx-auto px-4 py-8 pt-24">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              Histórico de Moedas Recebidas
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Acompanhe todas as moedas que você recebeu dos professores
            </p>
          </div>
          <div className="flex items-center space-x-4">
            <div className="bg-gradient-to-r from-green-600 to-emerald-600 text-white px-6 py-4 rounded-lg shadow-lg">
              <div className="text-sm font-medium mb-1">Seu Saldo</div>
              <div className="text-3xl font-bold">{saldoAluno.toFixed(2)} moedas</div>
            </div>
            <Link
              href="/aluno/vantagens"
              className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Ver Vantagens
            </Link>
          </div>
        </div>

        {/* Histórico */}
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
          {historico.length === 0 ? (
            <div className="text-center py-12">
              <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                Nenhuma moeda recebida ainda
              </h3>
              <p className="text-gray-500 dark:text-gray-400">
                Você ainda não recebeu nenhuma moeda dos professores.
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700">
                <thead className="bg-gray-50 dark:bg-gray-700">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Data
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Professor
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Quantidade
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Motivo
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                  {historico.map((transacao) => (
                    <tr key={transacao.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {new Date(transacao.dataTransacao).toLocaleString('pt-BR')}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                        {transacao.professorNome}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200">
                          +{transacao.quantidade.toFixed(2)} moedas
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                        {transacao.motivo || '-'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default HistoricoMoedasPage;

