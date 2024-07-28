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
import { useNavigate} from "react-router-dom"
import { ApiResponse, requestForResetPasswordToken} from "@/api/api"
import { useToast } from "@/components/ui/use-toast"

const formSchema = z.object({
    email:z.string().email("Invalid email addresss")
})

type Props = {}



export default function ForgotPassword({ }: Props) {
    const { toast } = useToast()
    const navigate = useNavigate()
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email:""
        },
    })

    // 2. Define a submit handler.
    async function onSubmit(values: z.infer<typeof formSchema>) {
        const response: ApiResponse = await requestForResetPasswordToken(values.email)
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
    return (
        <div className=" w-full md:h-full p-2 md:flex md:flex-row md:justify-center">
            <div className=" flex flex-col gap-3 md:w-[400px]">
                <div className=" w-full flex flex-col items-center">
                    Forgot Password
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
                        
                        <Button type="submit">Get change password link</Button>

                    </form>
                </Form>
            </div>
        </div>
    )
}