import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.rzrTest.payment',
  appName: 'RazerPaymentWithoutPlugin',
  webDir: 'www',
  server: {
    androidScheme: 'https'
  }
};

export default config;
