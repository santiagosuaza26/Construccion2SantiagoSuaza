'use client'

import { useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { auth } from '@/lib/auth'
import Layout from '@/components/Layout'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const router = useRouter()

  useEffect(() => {
    if (!auth.isAuthenticated()) {
      router.push('/login')
    }
  }, [router])

  if (!auth.isAuthenticated()) {
    return null // or a loading spinner
  }

  return <Layout title="Dashboard">{children}</Layout>
}