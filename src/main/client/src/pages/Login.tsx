import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"


import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import axios from "axios"
import { Checkbox } from "@/components/ui/checkbox"
import { Label } from "@/components/ui/label"
import { useState } from "react"
import { Link } from "react-router-dom"
import { useToast } from "@/components/ui/use-toast"

const formSchema = z.object({
  email: z.string(),
  password: z.string()
})

type Props = {}



export default function Login({ }: Props) {
  const { toast } = useToast()
  const [showPassword, setshowPassword] = useState(false)
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: ""
    },
  })

  // 2. Define a submit handler.
  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const response = await axios.post('/login', null, {
        params: {
          username: values.email,
          password: values.password
        }
      });
      if (response.status === 200) {
        window.location.href = '/';
      } else {
        toast({
          title: "Login failed",
          description: "Invalid credentials or Unverified user",
          variant: "destructive"
        })
      }
    } catch (error) {
      toast({
        title: "Login failed",
        description: "Invalid credentials or Unverified user",
        variant: "destructive"
      })
    }
  }
  return (
    <div className=" w-full md:h-full p-2 md:flex md:flex-row md:justify-center">
      <div className=" flex flex-col gap-3 md:w-[400px]">
        <div className=" w-full flex flex-col items-center">
          Login
        </div>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input placeholder="Email" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input placeholder="Password" type={showPassword ? "text" : "password"} {...field} />
                  </FormControl>
                  <FormMessage />
                  <Link to={"/forgotPassword"}><Label className=" hover:underline ">Forgot password</Label></Link>
                </FormItem>
              )}
            />
            <div className=" flex flex-row gap-3">
              <Checkbox onCheckedChange={() => {
                setshowPassword(!showPassword)
              }} />
              <Label >Show Password</Label>
            </div>
            <Button type="submit">Login</Button>
            <hr />
            <Button onClick={e => {
              e.preventDefault()
              window.location.href = "/oauth2/authorization/google"
            }}>Google</Button>
            <Button onClick={e => {
              e.preventDefault()
              window.location.href = "/oauth2/authorization/github"
            }} >GitHub</Button>
          </form>
          <Label>Does not have an account. <Link className=" hover:underline" to={"/signUp"}>Create Account </Link></Label>
        </Form>
      </div>
    </div>
  )
}