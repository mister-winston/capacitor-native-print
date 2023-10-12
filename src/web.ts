import { WebPlugin } from '@capacitor/core';

import type { NativePrintPlugin } from './definitions';

export class NativePrintWeb extends WebPlugin implements NativePrintPlugin {
  async print(): Promise<void> {
    const promise = new Promise<void>(resolve => {
      window.addEventListener(
        'afterprint',
        function handler() {
          window.removeEventListener('afterprint', handler);
          resolve();
        },
        { once: true },
      );
    });

    window.print();

    return promise;
  }
}
