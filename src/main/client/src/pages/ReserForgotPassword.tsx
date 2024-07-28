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
import { useNavigate, useParams } from "react-router-dom"
import { useState } from "react"
import { Label } from "@/components/ui/label"
import { ApiResponse, resetForgotenPassword } from "@/api/api"
import { useToast } from "@/components/ui/use-toast"
import { Checkbox } from "@/components/ui/checkbox"

const formSchema = z.object({
    password: z.string(),
    confirmPassword: z.string()
})

type Props = {}



export default function ResetForgotPassword({ }: Props) {
    const [showPassword, setshowPassword] = useState(false)
    const { toast } = useToast()
    const navigate = useNavigate()
    const [message, setmessage] = useState(false)
    const { userId, verifyCode } = useParams()
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            confirmPassword: "",
            password: ""
        },
    })

    // 2. Define a submit handler.
    async function onSubmit(values: z.infer<typeof formSchema>) {
        if (values.password !== values.confirmPassword) {
            setmessage(true)
            return
        }
        else {
            setmessage(false)
        }
        if (!message) {
            const response: ApiResponse = await resetForgotenPassword(userId || "", verifyCode || "", values.password)
            if (response.success) {
                //do a tost `
                toast({
                    title: "Success",
                    description: response.message
                })
                navigate("/")
            } else {
                //do a negative tost and diaplay message
                toast({
                    title: "Failed",
                    description: response.message
                    , variant: "destructive"
                })
            }
        }

    }
    return (
        <div className=" w-full md:h-full p-2 md:flex md:flex-row md:justify-center">
            <div className=" flex flex-col gap-3 md:w-[400px]">
                <div className=" w-full flex flex-col items-center">
                    ResetPassword
                </div>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-5">
                        <FormField
                            control={form.control}
                            name="password"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Password</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Password" type={showPassword?"text":"password"} {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="confirmPassword"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Confirm Password</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Confirm Password" type={showPassword?"text":"password"} onChangeCapture={e => {
                                            e.preventDefault()

                                        }} {...field} />
                                    </FormControl>
                                    {message && <Label className=" text-red-800">*Password do not match </Label>}
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <div className=" flex flex-row gap-3">
                            <Checkbox onCheckedChange={()=>{
                                setshowPassword(!showPassword)
                            }} />
                            <Label >Show Password</Label>
                        </div>
                        <Button type="submit">Reset Password</Button>

                    </form>
                </Form>
            </div>
        </div>
    )
}