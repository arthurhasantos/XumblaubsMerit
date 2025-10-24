"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";

const ClientesPage = () => {
  const router = useRouter();

  useEffect(() => {
    // Redirecionar para o painel admin
    router.push('/admin');
  }, [router]);

  return (
    <ProtectedRoute requireAdmin>
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default ClientesPage;
