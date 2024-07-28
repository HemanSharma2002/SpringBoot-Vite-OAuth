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
import { useToast } from "@/components/ui/use-toast"
import { ApiResponse, signUp } from "@/api/api"
import { useState } from "react"
import { Checkbox } from "@/components/ui/checkbox"
import { Label } from "@/components/ui/label"
import { Link } from "react-router-dom"

const formSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Password must be of minimum 8 charcaters"),
  name: z.string().min(1, "Name should not be empty")
})

type Props = {}



export default function SignUp({ }: Props) {
  const [showPassword, setshowPassword] = useState(false)
  const { toast } = useToast()
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
      name: ""
    },
  })

  // 2. Define a submit handler.
  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const response: ApiResponse = await signUp(values.email, values.password, values.name)
      if (response.success) {
        toast({
          title: "Success",
          description: response.message
        })
      } else {
        toast({
          title: "Failed",
          description: response.message,
          variant: "destructive"
        })
      }
    } catch (error) {
      console.log("failed");

    }
  }
  return (
    <div className=" w-full md:h-full p-2 md:flex md:flex-row md:justify-center">
      <div className=" flex flex-col gap-3 md:w-[400px]">
        <div className=" w-full flex flex-col items-center">
          Create Account
        </div>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-5">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
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
                </FormItem>
              )}
            />
            <div className=" flex flex-row gap-3">
              <Checkbox onCheckedChange={() => {
                setshowPassword(!showPassword)
              }} />
              <Label >Show Password</Label>
            </div>
            <Button type="submit">Create Account</Button>
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
          <Label>Already have an account. <Link className=" hover:underline" to={"/login"}>Login </Link></Label>
        </Form>
      </div>
    </div>
  )
}