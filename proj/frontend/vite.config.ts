import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// In development (npm run dev) Vite forwards /api to a backend on localhost:8080,
// the same path rule nginx applies in the container.
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
