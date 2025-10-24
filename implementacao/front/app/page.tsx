import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Sistema de Mérito",
  description: "Sistema de mérito com moedas virtuais para reconhecimento de estudantes",
  // other metadata
};

export default function Home() {
  return (
    <div className="container mx-auto px-4 py-8 pt-24">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-4">
          Sistema de Mérito
        </h1>
        <p className="text-lg text-gray-600 dark:text-gray-400 mb-8">
          Sistema de reconhecimento de estudantes com moedas virtuais
        </p>
        <div className="space-x-4">
          <a
            href="/signin"
            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-medium transition-colors"
          >
            Fazer Login
          </a>
          <a
            href="/signup"
            className="bg-gray-600 hover:bg-gray-700 text-white px-6 py-3 rounded-lg font-medium transition-colors"
          >
            Cadastrar
          </a>
        </div>
      </div>
    </div>
  );
}
