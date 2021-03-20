import {Directive, HostListener, Input, OnChanges, SimpleChanges} from '@angular/core';
import {NgControl} from '@angular/forms';

@Directive({
  selector: '[mcCharactersRestriction]'
})
export class CharactersRestrictionDirective implements OnChanges {

  @Input('mcCharactersRestriction') allowedCharacters = 'A-Za-z';
  private patternRegex!: RegExp;
  private replacePatternRegex!: RegExp;

  constructor(private ngControl: NgControl) {
  }

  @HostListener('ngModelChange', ['$event'])
  onModelChange(value: string) {
    const newValue = this.patternRegex.test(value) ? value : value.replace(this.replacePatternRegex, '');
    this.ngControl.valueAccessor?.writeValue(newValue);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.patternRegex = new RegExp('^[' + this.allowedCharacters + ']$');
    this.replacePatternRegex = new RegExp('[^' + this.allowedCharacters + ']', 'g');
  }
}
