import { WebPlugin } from '@capacitor/core';

import type { NativePrintPlugin } from './definitions';

export class NativePrintWeb extends WebPlugin implements NativePrintPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
