"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import Link from "next/link";

const AlunoPage = () => {
  const { user } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (user && user.roles.includes('ALUNO')) {
      // Redirecionar para a página de vantagens
      router.push('/aluno/vantagens');
    }
  }, [user, router]);

  return (
    <ProtectedRoute>
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default AlunoPage;

