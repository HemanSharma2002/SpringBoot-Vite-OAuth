
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import Home from './pages/Home.tsx'
import Login from './pages/Login.tsx'
import ResetForgotPassword from './pages/ReserForgotPassword.tsx'
import SignUp from './pages/signUp.tsx'
import ForgotPassword from './pages/ForgotPassword.tsx'

const router=createBrowserRouter(
  createRoutesFromElements(
    <Route path='' element={<App/>}>
      <Route path='' element={<Home/>}/>
      <Route path='/login' element={<Login/>}/>
      <Route path={`/resetPassword/:userId/:verifyCode`} element={<ResetForgotPassword/>}/>
      <Route path='/signUp' element={<SignUp/>}/>
      <Route path='/forgotPassword' element={<ForgotPassword/>}/>
    </Route>
  )
)
ReactDOM.createRoot(document.getElementById('root')!).render(
  <RouterProvider router={router}/>
  ,
)
