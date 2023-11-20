import { registerPlugin } from "@capacitor/core";

export interface RazerPaymentPlugin{
    onCreate(options: { value: string }): Promise<{ results: any[] }>;
    refreshtoken(options: { value: string }): Promise<{ results: any[] }>;
    starttranstion(options: { value: string }): Promise<{ results: any[] }>;
    loadLogs(): Promise<{ results: any[] }>;
}

const razerPaymentStatus = registerPlugin<RazerPaymentPlugin>('RazerPaymentStatus');

export default razerPaymentStatus;