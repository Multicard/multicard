import {Directive, ElementRef, HostListener, Input} from '@angular/core';

@Directive({
  selector: '[mcPatternRestriction]'
})
export class PatternRestrictionDirective {

  @Input('mcPatternRestriction') patternRestriction!: string;

  private element: ElementRef;

  constructor(element: ElementRef) {
    this.element = element;
  }

  @HostListener('keypress', ['$event'])
  handleKeyPress(event: KeyboardEvent) {
    return this.handleInputData(event, event.key);
  }

  @HostListener('paste', ['$event'])
  handlePaste(event: ClipboardEvent) {
    return this.handleInputData(event, event?.clipboardData?.getData('text'));
  }

  private handleInputData(event: Event, data?: string) {
    const regex = new RegExp(this.patternRestriction);
    if (!data || regex.test(data)) {
      return true;
    }

    event.preventDefault();
    return false;
  }
}
