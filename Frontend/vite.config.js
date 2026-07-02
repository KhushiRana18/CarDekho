import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/recommend': 'http://localhost:8080',
      '/cars': 'http://localhost:8080',
      '/api': 'http://localhost:8080'
    }
  }
})
