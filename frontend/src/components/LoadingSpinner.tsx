interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg'
  message?: string
}

export default function LoadingSpinner({ size = 'md', message = 'Cargando...' }: LoadingSpinnerProps) {
  const sizeClasses = {
    sm: 'h-4 w-4',
    md: 'h-8 w-8',
    lg: 'h-12 w-12'
  }

  return (
    <div className="flex flex-col items-center justify-center p-4" role="status" aria-live="polite">
      <div
        className={`animate-spin rounded-full border-b-2 border-blue-600 ${sizeClasses[size]}`}
        aria-hidden="true"
      ></div>
      {message && (
        <p className="mt-2 text-sm text-gray-600">{message}</p>
      )}
      <span className="sr-only">{message}</span>
    </div>
  )
}