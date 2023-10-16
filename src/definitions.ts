export enum PrintOrientation {
  Portrait = 'portrait',
  Landscape = 'landscape',
}

export enum PrintPageSize {
  A0 = 'a0',
  A1 = 'a1',
  A2 = 'a2',
  A3 = 'a3',
  A4 = 'a4',
  A5 = 'a5',
  A6 = 'a6',
  A7 = 'a7',
  A8 = 'a8',
  A9 = 'a9',
  A10 = 'a10',
  B0 = 'b0',
  B1 = 'b1',
  B2 = 'b2',
  B3 = 'b3',
  B4 = 'b4',
  B5 = 'b5',
  B6 = 'b6',
  B7 = 'b7',
  B8 = 'b8',
  B9 = 'b9',
  B10 = 'b10',
  C0 = 'c0',
  C1 = 'c1',
  C2 = 'c2',
  C3 = 'c3',
  C4 = 'c4',
  C5 = 'c5',
  C6 = 'c6',
  C7 = 'c7',
  C8 = 'c8',
  C9 = 'c9',
  C10 = 'c10',
  Government = 'govt',
  ThreeByFive = 'three_by_five',
  FourBySix = 'four_by_six',
  FiveByEight = 'five_by_eight',
  JuniorLegal = 'junior_legal',
  Ledger = 'ledger',
  Legal = 'legal',
  Letter = 'letter',
  Monarch = 'monarch',
  Tabloid = 'tabloid',
}

/** These settings do nothing on the web */
export type PrintOptions = {
  /** The name of the printed file */
  name: string;
  /**
   * Determines if the document should be printed in black and white
   * @default false
   * */
  monochrome?: boolean;
  /**
   * @default PrintOrientation.Portrait
   * */
  orientation?: PrintOrientation;
  /**
   * @default PrintPageSize.A4
   * */
  pageSize?: PrintPageSize;
};

export type AndroidPrintResult = {
  isBlocked: boolean;
  isCancelled: boolean;
  isCompleted: boolean;
  isFailed: boolean;
  isQueued: boolean;
  isStarted: boolean;
  copies: number;
  printerId: string;
  label: string;
  creationTime: number;
  state: number;
  pages?: { start: number; end: number }[];
};

export type IOSPrintResult = {
  printed: boolean;
}

export type WebPrintResult = void;

export type PrintResult = AndroidPrintResult | IOSPrintResult | WebPrintResult;

export interface NativePrintPlugin {
  print(options?: PrintOptions): Promise<PrintResult>;
}
