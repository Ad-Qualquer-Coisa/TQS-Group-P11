import { useEffect, useState } from "react";

// Walking skeleton: shows whether the frontend reaches the backend and the backend reaches the database.
export default function App() {
  const [status, setStatus] = useState("checking...");

  useEffect(() => {
    fetch("/api/actuator/health")
      .then((response) => response.json())
      .then((body: { status: string }) => setStatus(body.status))
      .catch(() => setStatus("unreachable"));
  }, []);

  return (
    <main>
      <h1>TQS P11</h1>
      <p>Backend status: {status}</p>
    </main>
  );
}
