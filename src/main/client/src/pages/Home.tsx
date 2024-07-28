import { ApiResponse, getUserDetails, User } from "@/api/api"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router"

type Props = {}

export default function Home({ }: Props) {
  const navigate=useNavigate()
  const [user, setuser] = useState<User>()
  useEffect(() => {
    loadpage()
  }, [])
  async function loadpage() {
    const resp: ApiResponse = await getUserDetails()
    if (resp.success) {
      setuser(resp.user)
    }
    else {
      return
    }
  }
  return (
    <main>
      {
        user ?
          <main>
            <button onClick={() => {
              window.location.href = (`/logout`)
            }}>Logout</button>
            <section>
              <p>User info</p>
              <img src={user?.picture} alt="profile" />
              <p>{user?.id}</p>
              <p>{user?.name}</p>
              <p>{user?.email}</p>
              <p>{user?.isEnabled}</p>
            </section>
          </main>
          :
          <main>
            <button onClick={()=>navigate("/login")}>login</button>
          </main>
      }
    </main>
  )
}