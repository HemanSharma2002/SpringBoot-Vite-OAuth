import axios from "axios"

export interface User {
    id: number;
    name: string;
    email: string;
    picture: string;
    isEnabled: boolean;
  }
  

export interface ApiResponse {
    success: boolean,
    message: string,
    user?:User

}

// @GetMapping("/v1/api/getUserDetails")
export const getUserDetails = async (): Promise<ApiResponse> => {
    try {
        const resp = await axios.get<ApiResponse>(`/v1/api/getUserDetails`, {
            withCredentials:true
        }).then(resp => resp.data)
        return resp
    } catch (error) {
        const resp: ApiResponse = {
            success: false,
            message: "Server Error"
        }
        return resp
    }
}

// @PathVariable Long userId, @PathVariable String verifyCode,@RequestParam String password
// /reset-password/{userId}/{verifyCode}

export const resetForgotenPassword = async (userId: string, verifyCode: string, password: string): Promise<ApiResponse> => {
    try {
        const resp = await axios.post<ApiResponse>(`/reset-password/${userId}/${verifyCode}`, null, {
            params: {
                password
            }
        }).then(resp => resp.data)
        return resp
    } catch (error) {
        const resp: ApiResponse = {
            success: false,
            message: "Server Error"
        }
        return resp
    }
}

// @PostMapping("/signup")
// (@RequestParam String email,@RequestParam String password,@RequestParam String name)

export const signUp = async (email:string,password:string,name:string): Promise<ApiResponse> => {
    try {
        const resp = await axios.post<ApiResponse>(`/signup`, null, {
            params: {
                email,
                password,
                name
            }
        }).then(resp => resp.data)
        console.log(resp);
        
        return resp
    } catch (error) {
        const resp: ApiResponse = {
            success: false,
            message: "Server Error"
        }
        return resp
    }
}

// @PostMapping("/reset-password")
// (@RequestParam String email)
export const requestForResetPasswordToken= async (email:string): Promise<ApiResponse> => {
    try {
        const resp = await axios.post<ApiResponse>(`/reset-password`, null, {
            params: {
                email,
            }
        }).then(resp => resp.data)
        return resp
    } catch (error) {
        const resp: ApiResponse = {
            success: false,
            message: "Server Error"
        }
        return resp
    }
}