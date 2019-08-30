/*
 * IdeaVim - Vim emulator for IDEs based on the IntelliJ platform
 * Copyright (C) 2003-2019 The IdeaVim authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.maddyhome.idea.vim.action.motion.select

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.action.VimCommandAction
import com.maddyhome.idea.vim.action.VimCommandActionBase
import com.maddyhome.idea.vim.command.Command
import com.maddyhome.idea.vim.command.CommandState
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.group.visual.vimSetSystemSelectionSilently
import com.maddyhome.idea.vim.handler.VimActionHandler
import com.maddyhome.idea.vim.helper.EditorHelper
import javax.swing.KeyStroke

/**
 * @author Alex Plate
 */

class SelectEnableLineModeAction : VimCommandAction() {
  override fun makeActionHandler(): VimActionHandler = object : VimActionHandler.SingleExecution() {


    override val mappingModes: MutableSet<MappingMode> = MappingMode.N

    override val keyStrokesSet: Set<List<KeyStroke>> = VimCommandActionBase.parseKeysSet("gH")

    override val type: Command.Type = Command.Type.OTHER_READONLY
    override fun execute(editor: Editor, context: DataContext, cmd: Command): Boolean {
      editor.caretModel.runForEachCaret { caret ->
        val lineEnd = EditorHelper.getLineEndForOffset(editor, caret.offset)
        val lineStart = EditorHelper.getLineStartForOffset(editor, caret.offset)
        caret.vimSetSystemSelectionSilently(lineStart, lineEnd)
      }
      return VimPlugin.getVisualMotion().enterSelectMode(editor, CommandState.SubMode.VISUAL_LINE)
    }
  }
}
