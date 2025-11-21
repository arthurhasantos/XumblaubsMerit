"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";
import { api } from "@/lib/api";

interface Professor {
  id: number;
  nome: string;
  email: string;
  saldoMoedas: number;
  instituicaoId: number;
  instituicaoNome: string;
}

interface Aluno {
  id: number;
  nome: string;
  email: string;
  curso: string;
  saldoMoedas: number;
  instituicaoId: number;
}

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

const ProfessorPage = () => {
  const { user, token } = useAuth();
  const router = useRouter();
  const [professor, setProfessor] = useState<Professor | null>(null);
  const [alunos, setAlunos] = useState<Aluno[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [selectedAluno, setSelectedAluno] = useState<Aluno | null>(null);
  const [quantidadeMoedas, setQuantidadeMoedas] = useState("");
  const [motivo, setMotivo] = useState("");
  const [sending, setSending] = useState(false);
  const [historico, setHistorico] = useState<Transacao[]>([]);
  const [loadingHistorico, setLoadingHistorico] = useState(false);

  useEffect(() => {
    if (user && user.roles.includes('PROFESSOR')) {
      fetchProfessorData();
    } else if (user && !user.roles.includes('PROFESSOR')) {
      toast.error('Acesso negado. Apenas professores podem acessar esta página.');
      router.push('/');
    }
  }, [user, token, router]);

  useEffect(() => {
    if (professor) {
      fetchAlunos();
      fetchHistorico();
    }
  }, [professor]);

  const fetchProfessorData = async () => {
    try {
      const data = await api.get('/professores/me');
      setProfessor(data);
    } catch (error) {
      console.error('Erro ao buscar dados do professor:', error);
      toast.error('Erro ao carregar dados do professor');
    } finally {
      setLoading(false);
    }
  };

  const fetchAlunos = async () => {
    try {
      const data = await api.get('/alunos');
      // Filtrar apenas alunos da mesma instituição do professor
      if (professor) {
        const alunosFiltrados = data.filter((aluno: Aluno) => 
          aluno.instituicaoId === professor.instituicaoId
        );
        setAlunos(alunosFiltrados);
      } else {
        setAlunos(data);
      }
    } catch (error) {
      console.error('Erro ao buscar alunos:', error);
      toast.error('Erro ao carregar lista de alunos');
    }
  };

  const fetchHistorico = async () => {
    setLoadingHistorico(true);
    try {
      const data = await api.get('/transacoes/professor');
      setHistorico(data);
    } catch (error) {
      console.error('Erro ao buscar histórico:', error);
      toast.error('Erro ao carregar histórico de transações');
    } finally {
      setLoadingHistorico(false);
    }
  };

  const openModal = (aluno: Aluno) => {
    setSelectedAluno(aluno);
    setQuantidadeMoedas("");
    setMotivo("");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedAluno(null);
    setQuantidadeMoedas("");
    setMotivo("");
  };

  const handleTransferirMoedas = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!selectedAluno) return;
    
    const quantidade = parseFloat(quantidadeMoedas);
    
    if (isNaN(quantidade) || quantidade <= 0) {
      toast.error('Por favor, insira uma quantidade válida maior que zero');
      return;
    }
    
    if (professor && quantidade > professor.saldoMoedas) {
      toast.error('Saldo insuficiente. Você não possui moedas suficientes.');
      return;
    }

    setSending(true);
    
    try {
      await api.post('/professores/transferir-moedas', {
        alunoId: selectedAluno.id,
        quantidade: quantidade,
        motivo: motivo || null
      });
      
      toast.success(`Moedas enviadas com sucesso para ${selectedAluno.nome}!`, {
        duration: 4000,
      });
      
      closeModal();
      // Atualizar dados
      fetchProfessorData();
      fetchAlunos();
      fetchHistorico();
    } catch (error: any) {
      console.error('Erro ao transferir moedas:', error);
      const errorMessage = error.message || 'Erro ao transferir moedas';
      toast.error(errorMessage, {
        duration: 4000,
      });
    } finally {
      setSending(false);
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
        {/* Header com saldo */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              Painel do Professor
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              {professor?.nome} - {professor?.instituicaoNome}
            </p>
          </div>
          <div className="bg-gradient-to-r from-yellow-500 to-orange-500 text-white px-6 py-4 rounded-lg shadow-lg">
            <div className="text-sm font-medium mb-1">Seu Saldo de Moedas</div>
            <div className="text-3xl font-bold">
              {professor?.saldoMoedas.toFixed(2) || '0.00'} moedas
            </div>
          </div>
        </div>

        {/* Seção de alunos */}
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6 mb-6">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
            Alunos Disponíveis
          </h2>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            Selecione um aluno para enviar moedas como reconhecimento
          </p>

          {alunos.length === 0 ? (
            <div className="text-center py-12">
              <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                Nenhum aluno encontrado
              </h3>
              <p className="text-gray-500 dark:text-gray-400">
                Não há alunos cadastrados no sistema.
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700">
                <thead className="bg-gray-50 dark:bg-gray-700">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Nome
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Email
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Curso
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Saldo Atual
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                  {alunos.map((aluno) => (
                    <tr key={aluno.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                        {aluno.nome}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                        {aluno.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                        {aluno.curso}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200">
                          {aluno.saldoMoedas.toFixed(2)} moedas
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => openModal(aluno)}
                          disabled={!professor || professor.saldoMoedas <= 0}
                          className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                            professor && professor.saldoMoedas > 0
                              ? 'bg-blue-600 hover:bg-blue-700 text-white'
                              : 'bg-gray-300 dark:bg-gray-700 text-gray-500 dark:text-gray-400 cursor-not-allowed'
                          }`}
                        >
                          Enviar Moedas
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Seção de Histórico */}
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
            Histórico de Transações
          </h2>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            Todas as moedas que você recebeu do sistema e enviou para os alunos
          </p>

          {loadingHistorico ? (
            <div className="text-center py-12">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
            </div>
          ) : historico.length === 0 ? (
            <div className="text-center py-12">
              <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                Nenhuma transação encontrada
              </h3>
              <p className="text-gray-500 dark:text-gray-400">
                Você ainda não realizou nenhuma transação de moedas.
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
                      Tipo
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Quantidade
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Aluno/Origem
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
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          transacao.tipo === 'ENVIADA' 
                            ? 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200' 
                            : 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
                        }`}>
                          {transacao.tipo === 'ENVIADA' ? 'Enviada' : 'Recebida'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                        {transacao.quantidade.toFixed(2)} moedas
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                        {transacao.tipo === 'ENVIADA' ? transacao.alunoNome : 'Sistema'}
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

        {/* Modal para enviar moedas */}
        {showModal && selectedAluno && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4">
              {/* Header do Modal */}
              <div className="flex justify-between items-center p-6 border-b border-gray-200 dark:border-gray-700">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                  Enviar Moedas para {selectedAluno.nome}
                </h3>
                <button
                  onClick={closeModal}
                  className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
                >
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              {/* Conteúdo do Modal */}
              <form onSubmit={handleTransferirMoedas} className="p-6">
                <div className="mb-4">
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    Quantidade de Moedas
                  </label>
                  <input
                    type="number"
                    step="0.01"
                    min="0.01"
                    max={professor?.saldoMoedas || 0}
                    value={quantidadeMoedas}
                    onChange={(e) => setQuantidadeMoedas(e.target.value)}
                    required
                    className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                    placeholder="0.00"
                  />
                  {professor && (
                    <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
                      Saldo disponível: {professor.saldoMoedas.toFixed(2)} moedas
                    </p>
                  )}
                </div>

                <div className="mb-6">
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    Motivo (opcional)
                  </label>
                  <textarea
                    value={motivo}
                    onChange={(e) => setMotivo(e.target.value)}
                    rows={3}
                    className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                    placeholder="Ex: Excelente desempenho na prova, participação ativa em aula..."
                  />
                </div>

                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={closeModal}
                    disabled={sending}
                    className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 bg-gray-100 dark:bg-gray-700 hover:bg-gray-200 dark:hover:bg-gray-600 rounded-lg transition-colors"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    disabled={sending || !quantidadeMoedas || parseFloat(quantidadeMoedas) <= 0}
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
                  >
                    {sending ? 'Enviando...' : 'Enviar Moedas'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </ProtectedRoute>
  );
};

export default ProfessorPage;

