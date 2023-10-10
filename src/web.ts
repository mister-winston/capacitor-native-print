import { WebPlugin } from '@capacitor/core';

import type { NativePrintPlugin } from './definitions';

export class NativePrintWeb extends WebPlugin implements NativePrintPlugin {
  async print(): Promise<void> {
    window.print();

    return new Promise(resolve => {
      window.addEventListener(
        'afterprint',
        function handler() {
          window.removeEventListener('afterprint', handler);
          resolve();
        },
        { once: true },
      );
    });
  }
}
