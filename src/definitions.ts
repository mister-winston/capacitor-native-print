export interface NativePrintPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
