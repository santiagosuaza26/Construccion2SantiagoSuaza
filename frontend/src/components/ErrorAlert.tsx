interface ErrorAlertProps {
  message: string
  onClose?: () => void
}

export default function ErrorAlert({ message, onClose }: ErrorAlertProps) {
  return (
    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4" role="alert">
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <svg className="w-5 h-5 mr-2 text-red-500" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
          </svg>
          <span>{message}</span>
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className="text-red-700 hover:text-red-900 focus:text-red-900 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 rounded"
            aria-label="Cerrar alerta de error"
          >
            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
            </svg>
          </button>
        )}
      </div>
    </div>
  )
}