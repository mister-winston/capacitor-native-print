import { registerPlugin } from '@capacitor/core';

import type { NativePrintPlugin } from './definitions';

const NativePrint = registerPlugin<NativePrintPlugin>('NativePrint', {
  web: () => import('./web').then(m => new m.NativePrintWeb()),
});

export * from './definitions';
export { NativePrint };
